package com.atguigu.gmall.client.list;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/18
 */
@RequestMapping("/api/es/sku")
@FeignClient("service-list")
public interface SkuEsListFeignClient {

    @PostMapping("/up")
    Result upSkuInfo(@RequestBody Goods goods);

    @GetMapping("/down/{skuId}")
    Result downSkuInfo(@PathVariable("skuId") Long skuId);


    @GetMapping("/incrScore/{skuId}/{hotScore}")
    Result incrScore(@PathVariable("skuId") Long skuId,
                            @PathVariable("hotScore") Long hotScore);
}
