package com.atguigu.gmall.payment.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gmall.client.order.OrderFeignClient;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.payment.config.properties.AlipayProperties;
import com.atguigu.gmall.payment.service.PayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
@Service
public class PayServiceImpl implements PayService {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Override
    public String pay(Long orderId) throws Exception {

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayProperties.getReturn_url());
        alipayRequest.setNotifyUrl(alipayProperties.getNotify_url());

        // 查询订单信息, 返回以下数据
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId);


        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = orderInfo.getOutTradeNo();
        //付款金额，必填
        String total_amount = orderInfo.getTotalAmount().toString();
        //订单名称，必填
        String subject = "尚品汇Gmall";
        //商品描述，可空
        String body = orderInfo.getTradeBody();
        String product_code = "FAST_INSTANT_TRADE_PAY";


        Map<String, String> map = new HashMap<>();

        map.put("out_trade_no", out_trade_no);
        map.put("total_amount", total_amount);
        map.put("subject", subject);
        map.put("body", body);
        map.put("product_code", product_code);

        alipayRequest.setBizContent(objectMapper.writeValueAsString(map));

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println(result);
        return result;
    }
}
