package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.api.AttrValueJsonVO;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.client.ProductFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        map.put("skuInfo",skuInfo);
        map.put("categoryView",categoryView);
        map.put("spuSaleAttrList",spuSaleAttr);
        map.put("valuesSkuJson",jsonStrResult);

        return map;

    }
}
