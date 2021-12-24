package com.atguigu.gmall.client.order;

import com.atguigu.gmall.model.order.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
@FeignClient("service-order")
@RequestMapping("/api/inner/order")
public interface OrderFeignClient {

    @GetMapping("/orderInfo/{orderId}")
    OrderInfo getOrderInfo(@PathVariable("orderId") Long orderId);
}
