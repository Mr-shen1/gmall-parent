package com.atguigu.gmall.list.service;

import com.atguigu.gmall.model.list.Goods;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/18
 */
public interface SkuEsService{


    /**
     * 上架商品
     * @param goods
     */
    void skuEsService(Goods goods);

    /**
     * 下架商品
     * @param skuId
     */
    void downSkuInfo(Long skuId);

    /**
     * 添加热度分
     * @param skuId
     * @param hotScore
     */
    void incrScore(Long skuId, Long hotScore);
}
