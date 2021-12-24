package com.atguigu.gmall.order.api;

import com.atguigu.gmall.model.api.ConfirmOrderVo;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@RequestMapping("/api/inner/order")
@RestController
public class OrderApiController {

    @Autowired
    private OrderInfoService orderInfoService;



    @GetMapping("/checkout/cart")
    public ConfirmOrderVo checkoutCart() {
        ConfirmOrderVo confirmOrderVo = orderInfoService.checkoutCart();


        return confirmOrderVo;
    }

    @GetMapping("/orderInfo/{orderId}")
    public OrderInfo checkoutCart(@PathVariable("orderId") Long orderId) {

        return orderInfoService.getById(orderId);

    }

}
