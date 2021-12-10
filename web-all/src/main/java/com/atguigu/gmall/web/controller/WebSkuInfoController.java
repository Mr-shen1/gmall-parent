package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.item.client.ItemFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@Controller
public class WebSkuInfoController {

    @Autowired
    private ItemFeignClient itemFeignClient;

    @GetMapping("{skuId}.html")
    public String skuDetail(@PathVariable("skuId")Long skuId, ModelMap modelMap) {

        //查询sku详情信息
        Map<String, Object> map = itemFeignClient.skuDetail(skuId);

        modelMap.addAllAttributes(map);

        return "item/index";
    }
}
