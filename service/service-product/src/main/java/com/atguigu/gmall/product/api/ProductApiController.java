package com.atguigu.gmall.product.client.api;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.ManageSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/09
 */
@RestController
@RequestMapping("/admin/product")
public class ProductApiController {

    @Autowired
    private ManageSevice manageSevice;

    @GetMapping("/inner/getSkuInfo/{skuId}")
    public SkuInfo getAttrValueList(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = manageSevice.getSkuInfo(skuId);
        return skuInfo;
    }


}
