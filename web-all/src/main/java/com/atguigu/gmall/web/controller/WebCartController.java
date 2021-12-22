package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.client.cart.CartFeignClient;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/22
 */

@Controller
public class WebCartController {

    @Autowired
    private CartFeignClient cartFeignClient;

    @GetMapping("/cart.html")
    public String toCart() {


        return "cart/index";
    }

    // addCart.html?skuId=54&skuNum=1
    @GetMapping("addCart.html")
    public String toAddCart(@RequestParam("skuNum") Integer skuNum,
                            @RequestParam("skuId") Long skuId,
                            @RequestHeader(value = "userId", required = false) String userId,
                            @RequestHeader(value = "userTempId", required = false) String userTempId,
                            ModelMap modelMap) {


        SkuInfo skuInfo = cartFeignClient.addCart(skuId, skuNum);

        modelMap.addAttribute("skuInfo", skuInfo);
        modelMap.addAttribute("skuNum", skuNum);

        return "cart/addCart";
    }
}
