package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/07
 */
@RestController
@RequestMapping("/admin/product")
public class SpuInfoController {

    @Autowired
    private SpuInfoService spuInfoService;
    @Autowired
    private BaseSaleAttrService baseSaleAttrService;
    @Autowired
    private BaseTrademarkService baseTrademarkService;

    @GetMapping("/{page}/{limit}")
    public Result getSpuInfoPage(@PathVariable("page") Long page,
                                 @PathVariable("limit") Long limit,
                                 @RequestParam("category3Id") Long category3Id) {
        Page<SpuInfo> pages = spuInfoService.getSpuInfoPage(page, limit, category3Id);
        return Result.ok(pages);
    }

    /**
     * 获取销售属性
     *
     * @return
     */
    @GetMapping("/baseSaleAttrList")
    public Result baseSaleAttrList() {
        List<BaseSaleAttr> list = baseSaleAttrService.list();
        return Result.ok(list);

    }

    /**
     * 获取品牌属性
     */
    @GetMapping("/baseTrademark/getTrademarkList")
    public Result getTrademarkList() {
        List<BaseTrademark> list = baseTrademarkService.list();
        return Result.ok(list);
    }

    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo) {

        if (spuInfo == null) {
            return Result.fail();
        }
        spuInfoService.saveSpuInfo(spuInfo);

        return Result.ok();
    }
}
