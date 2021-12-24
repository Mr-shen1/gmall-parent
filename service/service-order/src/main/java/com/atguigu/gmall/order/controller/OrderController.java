package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@RestController
@Slf4j
@RequestMapping("/api/order/auth")
public class OrderController {


    @Autowired
    private OrderInfoService orderInfoService;


    @PostMapping("submitOrder")
    public Result submitOrder(@RequestParam("tradeNo") String tradeNo,
                              @RequestBody OrderInfo orderInfo) {
        String userId = AuthContextHolder.getUserId();

        Long orderId = null;
        try {
            orderId = orderInfoService.submitOrder(orderInfo, tradeNo, userId);
            return Result.ok(orderId);
        } catch (GmallException e) {
            log.error("submitOrder() called with exception => 【tradeNo = {}】, 【orderInfo = {}】",tradeNo, orderInfo,e);
            Result fail = Result.fail();
            fail.setMessage(e.getMessage());
            fail.setCode(e.getCode());
            return fail;
        }

    }
}
