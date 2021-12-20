package com.atguigu.gmall.list.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.service.SkuEsService;
import com.atguigu.gmall.model.list.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/18
 */
@RestController
@RequestMapping("/api/es/sku")
public class SkuEsController {

    @Autowired
    private SkuEsService skuEsService;

    @PostMapping("/up")
    public Result upSkuInfo(@RequestBody Goods goods) {
        skuEsService.skuEsService(goods);
        return Result.ok();
    }

    @GetMapping("/down/{skuId}")
    public Result downSkuInfo(@PathVariable("skuId") Long skuId) {
        skuEsService.downSkuInfo(skuId);
        return Result.ok();
    }

    @GetMapping("/incrScore/{skuId}/{hotScore}")
    public Result incrScore(@PathVariable("skuId") Long skuId,
                            @PathVariable("hotScore") Long hotScore) {

        skuEsService.incrScore(skuId, hotScore);
        return Result.ok();
    }

}
