package com.atguigu.gmall.client.product;

import com.atguigu.gmall.model.api.AttrValueJsonVO;
import com.atguigu.gmall.model.api.CategoryVO;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@FeignClient("service-product")
@RequestMapping("/api/product")
public interface ProductFeignClient {


    @GetMapping("/index/allCategory")
    List<CategoryVO> getCategoryAll();


    @GetMapping("/detail/price/{skuId}")
    BigDecimal getSkuPriceById(@PathVariable("skuId") Long skuId);

    @GetMapping("/info/{skuId}")
    SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId);


    @GetMapping("/categoryView/{category3Id}")
    BaseCategoryView getCategoryView(@PathVariable("category3Id") Long category3Id);


    @GetMapping("/image/{skuId}")
    List<SkuImage> getSkuImageList(@PathVariable("skuId") Long skuId);


    @GetMapping("/saleattr/{spuId}/{skuId}")
    List<SpuSaleAttr> getSpuSaleAttrBySpuIdAndSkuId(@PathVariable("spuId") Long spuId,
                                                           @PathVariable("skuId") Long skuId);


    @GetMapping("json/{spuId}")
    List<AttrValueJsonVO> getAttrValueJsonVOList(@PathVariable("spuId") Long spuId);
}
