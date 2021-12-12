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
}
