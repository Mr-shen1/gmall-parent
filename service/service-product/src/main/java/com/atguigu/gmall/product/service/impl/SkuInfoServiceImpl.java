package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuAttrValue;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //1 添加sku_info信息
        baseMapper.insert(skuInfo);
        // 获取skuId和spuId
        Long skuId = skuInfo.getId();
        Long spuId = skuInfo.getSpuId();
        //2 添加sku_image信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        List<SkuImage>  skuImages = skuImageList.stream().map((e) -> {
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

    @Override
    public void updateSaleStatus(Long skuId, int i) {
        QueryWrapper<SkuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", skuId);
        SkuInfo skuInfo = baseMapper.selectOne(wrapper);
        skuInfo.setIsSale(i);
        baseMapper.updateById(skuInfo);
    }

    @Override
    public BigDecimal getSkuPriceById(Long skuId) {
        BigDecimal price = skuInfoMapper.getSkuPriceById(skuId);
        return price;
    }
}




