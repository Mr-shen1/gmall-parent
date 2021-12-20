package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.client.list.SkuEsListFeignClient;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.SkuAttrValue;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.atguigu.gmall.starter.cache.core.DbCacheConsensusService;
import com.atguigu.gmall.starter.redisson.properties.BloomFilterProperties;
import com.atguigu.gmall.starter.redisson.properties.BloomProperty;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author SSS
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2021-12-08 14:54:57
 */
@Transactional
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    @Autowired
    private SkuImageService skuImageService;
    @Autowired
    private SkuAttrValueService skuAttrValueService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Qualifier("skuBloomFilter")
    @Autowired
    private RBloomFilter<Object> rBloomFilter;

    @Autowired
    private BloomFilterProperties bloomFilterProperties;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private DbCacheConsensusService dbCacheConsensusService;

    @Autowired
    private SkuEsListFeignClient skuEsListFeignClient;

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //1 添加sku_info信息
        baseMapper.insert(skuInfo);
        // 获取skuId和spuId
        Long skuId = skuInfo.getId();
        Long spuId = skuInfo.getSpuId();
        //2 添加sku_image信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        List<SkuImage> skuImages = skuImageList.stream().map((e) -> {
            e.setSkuId(skuId);
            return e;
        }).collect(Collectors.toList());
        skuImageService.saveBatch(skuImages);
        //3 添加sku_attr_value信息
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        List<SkuAttrValue> skuAttrValues = skuAttrValueList.stream().map(e -> {
            e.setSkuId(skuId);
            return e;
        }).collect(Collectors.toList());
        List<SkuAttrValue> attrValues = skuAttrValues.stream().map(e -> {
            e.setSkuId(skuId);
            return e;
        }).collect(Collectors.toList());
        skuAttrValueService.saveBatch(attrValues);
        //4 添加sku_sale_attr_value信息
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        List<SkuSaleAttrValue> skuSaleAttrValues = skuSaleAttrValueList.stream().map(e -> {
            e.setSkuId(skuId);
            e.setSpuId(spuId);
            return e;
        }).collect(Collectors.toList());
        skuSaleAttrValueService.saveBatch(skuSaleAttrValues);

    }

    @Transactional
    @Override
    public void updateSaleStatus(Long skuId, int status) {
        QueryWrapper<SkuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", skuId);
        SkuInfo skuInfo = baseMapper.selectOne(wrapper);
        skuInfo.setIsSale(status);
        baseMapper.updateById(skuInfo);

        // 延迟双删
        dbCacheConsensusService.delayDoubleDelete(RedisConst.SKU_CACHE_PREFIX + skuId);


        // 商品上下架
        if (status == 1) {
            // 上架
            Goods goods = skuInfoMapper.getSkuInfo2Goods(skuId);
            skuEsListFeignClient.upSkuInfo(goods);
        } else {
            // 下架
            skuEsListFeignClient.downSkuInfo(skuId);

        }


    }

    @Override
    public BigDecimal getSkuPriceById(Long skuId) {
        BigDecimal price = skuInfoMapper.getSkuPriceById(skuId);
        return price;
    }


    /**
     * 每个3天自动重建布隆过滤器
     */
    @Override
    @Scheduled(cron = "0 0 3 */3 * ?")
    public void rebuildBloomFilter() {
        String rebuildLockKey = RedisConst.BLOOM_REBUILD_LOCK;

        RLock lock = redissonClient.getLock(rebuildLockKey);
        lock.lock();
        try {
            // 判断标志位
            String s = redisTemplate.opsForValue().get(RedisConst.BLOOM_STATUS);
            if (Objects.equals("1", s)) {
                return;
            }

            // 1 先删除原来的
            rBloomFilter.delete();
            // 2 初始化布隆过滤器
            BloomProperty skuBloomProperty = bloomFilterProperties.getConfig().get("sku");

            rBloomFilter.tryInit(skuBloomProperty.getExpectedInsertions(), skuBloomProperty.getFalseProbability());
            // 3 查数据库, 添加到布隆过滤器
            List<String> skuIds = skuInfoMapper.selectAllIds();
            for (String skuId : skuIds) {// 统一存String类型的数据
                rBloomFilter.add(skuId);
            }

            //存标志位
            redisTemplate.opsForValue().set(RedisConst.BLOOM_STATUS, "1", RedisConst.BLOOM_REBUILD_REBUILD_TIME, TimeUnit.SECONDS);

        } finally {
            lock.unlock();
        }
    }
}




