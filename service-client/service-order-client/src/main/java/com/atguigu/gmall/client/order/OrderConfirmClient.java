package com.atguigu.gmall.client.order;

import com.atguigu.gmall.model.api.ConfirmOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@FeignClient("service-order")
@RequestMapping("/api/inner/order")
public interface OrderConfirmClient {

    @GetMapping("/checkout/cart")
    ConfirmOrderVo checkoutCart();


}
