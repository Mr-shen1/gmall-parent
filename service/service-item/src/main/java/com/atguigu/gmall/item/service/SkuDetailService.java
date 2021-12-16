package com.atguigu.gmall.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
public interface SkuDetailService {

    /**
     * 查询详情信息
     * @param skuId
     * @return
     */
    Map<String, Object> getSkuDeatail(Long skuId) throws JsonProcessingException;


    /**
     * 从缓存中获取item信息
     * @param skuId
     * @return
     * @throws Exception
     */
    Map<String, Object> getSkuDeatailFromCache(Long skuId) throws Exception;

    /**
     * 异步方式获取详情信息
     * @param skuId
     * @return
     */
    Map<String, Object> getSkuDeatailAsync(Long skuId);
}
