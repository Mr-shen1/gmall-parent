package com.atguigu.gmall.item.api;

import com.atguigu.gmall.item.service.SkuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@RestController
@RequestMapping("/api/item")
public class ItemSkuDetailApiController {

    @Autowired
    private SkuDetailService skuDetailService;


    @GetMapping("/sku/detail/{skuId}")
    Map<String, Object> skuDetail(@PathVariable("skuId") Long skuId) throws Exception {
        //Map<String, Object> data = skuDetailService.getSkuDeatailFromCache(skuId);
        Map<String, Object> data = skuDetailService.getSkuDeatail(skuId);


        return data;
    }

}
