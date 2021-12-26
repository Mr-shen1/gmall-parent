package com.atguigu.gmall.payment.controller;

import com.atguigu.gmall.payment.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
@RestController
@RequestMapping("/api/payment/alipay")
public class PaymentController {

    @Autowired
    private PayService payService;



    @GetMapping("/submit/{orderId}")
    public String toPay(@PathVariable("orderId") Long orderId) throws Exception {

        String page = payService.pay(orderId);

        return page;
    }
}
