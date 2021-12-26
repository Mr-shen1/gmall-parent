package com.atguigu.gmall.payment.service;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
public interface PayService {


    /**
     * 跳转到支付页面
     * @param orderId
     * @return
     */
    String pay(Long orderId) throws Exception;
}
