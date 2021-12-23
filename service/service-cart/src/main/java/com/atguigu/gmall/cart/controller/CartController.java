package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/22
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {


    @Autowired
    private CartService cartService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/cartList")
    public Result cartList(@RequestHeader(value = "userId", required = false) String userId,
                           @RequestHeader(value = "userTempId", required = false) String userTempId) throws JsonProcessingException {
        String cartKey = cartService.determineCartKey(userId, userTempId);
        List<CartInfo> cartInfoList = cartService.getCartList(cartKey);

        // 合并购物车
        cartService.mergeCart(userId, userTempId);
        // 给一个过期时间
        if (!StringUtils.isEmpty(userTempId)) {
            stringRedisTemplate.expire(userTempId, 365, TimeUnit.DAYS);
        }

        return Result.ok(cartInfoList);
    }

    @PostMapping("/addToCart/{skuId}/{num}")
    public Result add2Cart(@PathVariable("skuId") Long skuId,
                           @PathVariable("num") Long num,
                           @RequestHeader(value = "userId", required = false) String userId,
                           @RequestHeader(value = "userTempId", required = false) String userTempId) throws JsonProcessingException {

        String cartKey = cartService.determineCartKey(userId, userTempId);

        cartService.add2CartNum(cartKey, skuId, num);
        // 给一个过期时间
        if (!StringUtils.isEmpty(userTempId)) {
            stringRedisTemplate.expire(userTempId, 365, TimeUnit.DAYS);
        }

        return Result.ok();
    }

    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(
            @RequestHeader(value = "userId", required = false) String userId,
            @RequestHeader(value = "userTempId", required = false) String userTempId,
            @PathVariable("skuId") Long skuId) {
        String cartKey = cartService.determineCartKey(userId, userTempId);
        cartService.deleteCart(cartKey, skuId);
        // 给一个过期时间
        if (!StringUtils.isEmpty(userTempId)) {
            stringRedisTemplate.expire(userTempId, 365, TimeUnit.DAYS);
        }

        return Result.ok();
    }

    @GetMapping("/checkCart/{skuId}/{status}")
    public Result checkCart(@PathVariable("skuId") Long skuId,
                            @PathVariable("status") Long status,
                            @RequestHeader(value = "userId", required = false) String userId,
                            @RequestHeader(value = "userTempId", required = false) String userTempId) throws JsonProcessingException {
        String cartKey = cartService.determineCartKey(userId, userTempId);

        cartService.updateCartCheckStatus(cartKey, skuId, status);
        // 给一个过期时间
        if (!StringUtils.isEmpty(userTempId)) {
            stringRedisTemplate.expire(userTempId, 365, TimeUnit.DAYS);
        }

        return Result.ok();
    }

}
