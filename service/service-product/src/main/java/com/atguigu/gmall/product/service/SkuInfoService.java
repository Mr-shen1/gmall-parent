package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author skf
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2021-12-08 14:54:57
*/
public interface SkuInfoService extends IService<SkuInfo> {

    /**
     * 添加sku信息
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 更改sku的上下架状态
     * @param skuId
     * @param i
     */
    void updateSaleStatus(Long skuId, int i);
}
