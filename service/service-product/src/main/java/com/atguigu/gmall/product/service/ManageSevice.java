package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuInfo;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/09
 */
public interface ManageSevice {


    /**
     * 获取sku信息
     * @return
     */
    SkuInfo getSkuInfo(Long skuId);
}
