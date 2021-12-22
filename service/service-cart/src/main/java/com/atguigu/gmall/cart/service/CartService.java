package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/22
 */
public interface CartService {


    /**
     * 判断CartKey的值
     * @param userId
     * @param userTempId
     * @return
     */
    String determineCartKey(String userId, String userTempId);

    /**
     * 添加购物车信息
     * @param skuId
     * @param skuNum
     * @param cartKey
     * @return
     */
    SkuInfo addCart(Long skuId, Integer skuNum, String cartKey) throws JsonProcessingException;

    /**
     * 查询购物车列表
     * @return
     */
    List<CartInfo> getCartList(String cartKey);

    /**
     * 增加或减少购物项的数量
     * @param cartKey
     * @param skuId
     * @param num
     */
    void add2CartNum(String cartKey, Long skuId, Long num) throws JsonProcessingException;

    /**
     * 删除购物车信息
     * @param cartKey
     * @param skuId
     */
    void deleteCart(String cartKey, Long skuId);

    /**
     * 更新选中状态
     * @param cartKey
     * @param skuId
     * @param status
     */
    void updateCartCheckStatus(String cartKey, Long skuId, Long status) throws JsonProcessingException;


    /**
     * 检查购物车最大数量
     *
     * @param cartKey
     */
    void checkCartSize(String cartKey);


    /**
     * 在登录状态下合并临时购物车
     * @param userId
     * @param userTempId
     */
    void mergeCart(String userId, String userTempId) throws JsonProcessingException;
}
