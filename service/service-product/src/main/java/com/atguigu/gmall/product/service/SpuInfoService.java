package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SpuInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/07
 */
public interface SpuInfoService extends IService<SpuInfo> {

    /**
     * 获取spu分页数据
     * @param page
     * @param limit
     * @param category3Id
     * @return
     */
    Page<SpuInfo> getSpuInfoPage(Long page, Long limit, Long category3Id);

    /**
     * 添加spu信息
     * @param spuInfo
     */
    void saveSpuInfo(SpuInfo spuInfo);
}
