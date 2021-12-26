package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.client.order.OrderFeignClient;
import com.atguigu.gmall.model.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@Controller
public class WebPaymentController {

    @Autowired
    private OrderFeignClient orderFeignClient;


    @GetMapping("/pay.html")
    public String paymentPage(@RequestParam("orderId") Long orderId, ModelMap modelMap) {

        BigDecimal totalAmount = orderFeignClient.getOrderTotalAmount(orderId);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setTotalAmount(totalAmount);

        modelMap.addAttribute("orderInfo", orderInfo);


        return "payment/pay";
    }
}
