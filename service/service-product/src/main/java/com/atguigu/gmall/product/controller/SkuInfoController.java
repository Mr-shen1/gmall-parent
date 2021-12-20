package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * desc: test
 *
 * @author: skf
 * @date: 2021/12/07
 */
@RestController
@RequestMapping("/admin/product")
public class SkuInfoController {

    @Autowired
    private SpuImageService spuImageService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;
    @Autowired
    private SkuInfoService skuInfoService;

    @Qualifier("skuBloomFilter")
    @Autowired
    private RBloomFilter<Object> rBloomFilter;

    @GetMapping("/spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable("spuId") Long spuId) {
        QueryWrapper<SpuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id", spuId);
        List<SpuImage> list = spuImageService.list(wrapper);
        return Result.ok(list);
    }

    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable("spuId") Long spuId) {
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrService.getSpuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }

    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        // 将skuId添加到布隆中
        skuInfoService.saveSkuInfo(skuInfo);

        Long skuId = skuInfo.getId();
        rBloomFilter.add(skuId.toString());
        return Result.ok();
    }

    @GetMapping("/list/{page}/{limit}")
    public Result getSkuInfoPages(@PathVariable("page") Long page, @PathVariable("limit") Long limit) {
        Page<SkuInfo> pageInfo = new Page<>(page, limit);

        Page<SkuInfo> pageResult = skuInfoService.page(pageInfo);
        return Result.ok(pageResult);

    }

    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.updateSaleStatus(skuId, 1);
        return Result.ok();
    }

    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.updateSaleStatus(skuId, 0);
        return Result.ok();
    }


}
