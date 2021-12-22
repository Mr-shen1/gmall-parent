package com.atguigu.gmall.client.cart;

import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@FeignClient("service-cart")
@RequestMapping("/api/inner/cart")
public interface CartFeignClient {


    @GetMapping("/add/cart/{skuId}/{skuNum}")
    SkuInfo addCart(@PathVariable("skuId") Long skuId,
                    @PathVariable("skuNum") Integer skuNum);



}
