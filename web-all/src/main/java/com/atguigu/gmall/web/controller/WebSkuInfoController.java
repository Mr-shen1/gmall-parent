package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.client.item.ItemFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public String skuDetail(@PathVariable("skuId")Long skuId, ModelMap modelMap) throws JsonProcessingException {

        //查询sku详情信息
        Map<String, Object> map = itemFeignClient.skuDetail(skuId);

        //ObjectMapper mapper = new ObjectMapper();

        ////重新把valueJson处理下
        //map.put("valuesSkuJson",mapper.writeValueAsString(map.get("valuesSkuJson")));

        modelMap.addAllAttributes(map);

        return "item/index";
    }
}
