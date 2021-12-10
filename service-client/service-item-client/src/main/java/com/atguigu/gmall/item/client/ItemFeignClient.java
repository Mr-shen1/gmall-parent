package com.atguigu.gmall.item.client;

import com.atguigu.gmall.model.api.CategoryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@FeignClient("service-item")
@RequestMapping("/api/item")
public interface ItemFeignClient {

    @GetMapping("/index/allCategory")
    List<CategoryVO> getCategoryAll();



    @GetMapping("/sku/detail/{skuId}")
    Map<String, Object> skuDetail(@PathVariable("skuId") Long skuId);
}
