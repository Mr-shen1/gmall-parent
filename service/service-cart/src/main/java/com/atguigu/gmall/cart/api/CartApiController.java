package com.atguigu.gmall.cart.api;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.model.product.SkuInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/22
 */
@RestController
@RequestMapping("/api/inner/cart")
public class CartApiController {

    @Autowired
    private CartService cartService;

    @GetMapping("/add/cart/{skuId}/{skuNum}")
    public SkuInfo addCart(@PathVariable("skuId") Long skuId,
                           @PathVariable("skuNum") Integer skuNum,
                           @RequestHeader(value = "userId", required = false) String userId,
                           @RequestHeader(value = "userTempId", required = false) String userTempId) throws JsonProcessingException {
        String cartKey = cartService.determineCartKey(userId, userTempId);

        SkuInfo skuInfo = cartService.addCart(skuId, skuNum, cartKey);

        return skuInfo;
    }
}
