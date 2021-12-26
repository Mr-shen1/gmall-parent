package com.atguigu.gmall.common.constant;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
public class RabbitMqConst {

    public static final String ORDER_EXCHANGE_NAME = "order-event-exchange";
    public static final String ORDER_DELAY_QUEUE = "order-delay-queue";
    public static final String ORDER_TIMEOUT_QUEUE = "order-timeout-queue";
    public static final String ORDER_DELAY_ROUNTING_KEY = "order.delay.rk";
    public static final String ORDER_TIMEOUT_ROUNTING_KEY = "order.timeout.rk";


}
