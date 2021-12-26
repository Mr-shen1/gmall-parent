package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.api.ConfirmOrderVo;
import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
* @author SSS
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service
* @createDate 2021-12-24 18:10:34
*/
public interface OrderInfoService extends IService<OrderInfo> {


    /**
     * 获取确认订单页面所需信息
     * @return
     */
    ConfirmOrderVo checkoutCart();

    /**
     * 提交订单
     * @param orderInfo
     * @param tradeNo
     * @param userId
     * @return
     */
    Long submitOrder(OrderInfo orderInfo, String tradeNo, String userId);


    /**
     * 保存订单
     * @param orderInfo
     * @param tradeNo
     * @return
     */
    Long saveOrder(OrderInfo orderInfo, String tradeNo);

    /**
     * 根据orderId获取总金额
     * @param orderId
     * @return
     */
    BigDecimal getOrderTotalAmount(Long orderId);


    void closeOrder(Long orderId, String orderStatus, String processStatus);

    /**
     * 获取totalAmount body 和 tradeNo
     * @param orderId
     * @return
     */
    OrderInfo getOrderInfo(Long orderId);
}
