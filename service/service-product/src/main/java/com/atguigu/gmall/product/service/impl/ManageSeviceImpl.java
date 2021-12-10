package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.mapper.SkuImageMapper;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.ManageSevice;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/09
 */
@Service
public class ManageSeviceImpl implements ManageSevice {

    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;


    @Override
    public SkuInfo getSkuInfo(Long skuId) {

        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        QueryWrapper<SkuImage> wrapper = new QueryWrapper<>();

        List<SkuImage> skuImages = skuImageMapper.selectList(wrapper);
        skuInfo.setSkuImageList(skuImages);

        return skuInfo;
    }
}
