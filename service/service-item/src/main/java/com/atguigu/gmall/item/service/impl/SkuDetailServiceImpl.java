package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.api.AttrValueJsonVO;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.client.ProductFeignClient;
import com.atguigu.gmall.starter.cache.annotation.BizLockOptions;
import com.atguigu.gmall.starter.cache.annotation.BloomOptions;
import com.atguigu.gmall.starter.cache.annotation.GmallCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Qualifier("skuBloomFilter")
    @Autowired
    private RBloomFilter<Object> rBloomFilter;


    @GmallCache(cacheKey = RedisConst.SKU_CACHE_PREFIX + "#{#args[0]}",
                lockOptions = @BizLockOptions(lockTime = 3, lockReleaseTime = 6),
                bloomOptions = @BloomOptions(bloomName = "sku-bloom", bloomExp = "#{#args[0]}")
                )
    @Override
    public Map<String, Object> getSkuDeatail(Long skuId) throws JsonProcessingException {

        Map<String, Object> map = new HashMap<>();
        // 查询价格
        BigDecimal price = productFeignClient.getSkuPriceById(skuId);

        //查询sku基本信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        Long category3Id = skuInfo.getCategory3Id();

        //获取三级分类信息
        BaseCategoryView categoryView = productFeignClient.getCategoryView(category3Id);

        //获取图片信息
        List<SkuImage> skuImageList = productFeignClient.getSkuImageList(skuId);
        skuInfo.setSkuImageList(skuImageList);

        //获取销售属性列表
        List<SpuSaleAttr> spuSaleAttr = productFeignClient.getSpuSaleAttrBySpuIdAndSkuId(skuInfo.getSpuId(), skuId);

        //获取销售属性切换所需的信息
        List<AttrValueJsonVO> jsonResult = productFeignClient.getAttrValueJsonVOList(skuInfo.getSpuId());

        Map<String, String> jsonMap = new HashMap<>();
        for (AttrValueJsonVO jsonVO : jsonResult) {

            jsonMap.put(jsonVO.getValueID(), jsonVO.getSkuID());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStrResult = objectMapper.writeValueAsString(jsonMap);

        map.put("price", price);
        map.put("skuInfo", skuInfo);
        map.put("categoryView", categoryView);
        map.put("spuSaleAttrList", spuSaleAttr);
        map.put("valuesSkuJson", jsonStrResult);

        return map;

    }

    @Override
    public Map<String, Object> getSkuDeatailFromCache(Long skuId) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String cacheKey = RedisConst.ITEMS_PREFIX + skuId;
        String lockKey = RedisConst.LOCK_PREFIX + RedisConst.SKU_LOCK + skuId; // 锁的粒度设计的很细, 精确到商品级别

        RLock lock = redissonClient.getLock(lockKey);

        // 先查缓存
        String cacheResult = redisTemplate.opsForValue().get(cacheKey);

        if (StringUtils.isEmpty(cacheResult)) {
            // 缓存中没有
            // 抢锁
            boolean locked = lock.tryLock(3, 6, TimeUnit.SECONDS);

            if (!locked) {
                // 没抢到锁
                cacheResult = redisTemplate.opsForValue().get(cacheKey);
                // 没有抢到锁, 查一次缓存直接返回
                return objectMapper.readValue(cacheResult, new TypeReference<Map<String, Object>>() {
                });
            }

            try {
                //再查一次缓存
                cacheResult = redisTemplate.opsForValue().get(cacheKey);
                if (StringUtils.isEmpty(cacheResult)) {
                    // 判断布隆过滤器中是否存在
                    boolean exist = rBloomFilter.contains(skuId.toString());
                    if (!exist) {
                        return null;
                    }

                    //查数据库
                    Map<String, Object> skuDeatail = getSkuDeatail(skuId);
                    long ttl = RedisConst.INDEX_CACHE_TIMEOUT;
                    if (skuDeatail == null) {
                        ttl = RedisConst.NULL_TIMEOUT;
                    }
                    // 存入缓存
                    redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(skuDeatail), ttl, TimeUnit.SECONDS);

                    return skuDeatail;

                } else {
                    return objectMapper.readValue(cacheResult, new TypeReference<Map<String, Object>>() {
                    });
                }
            }finally {
                try {
                    lock.unlock();
                } catch (Exception e) {

                }
            }

        } else {
            // 缓存中有, 直接返回
            return objectMapper.readValue(cacheResult, new TypeReference<Map<String, Object>>() {
            });

        }
    }
}
