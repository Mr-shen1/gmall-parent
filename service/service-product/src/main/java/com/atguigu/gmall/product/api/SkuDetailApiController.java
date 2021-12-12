package com.atguigu.gmall.product.api;

import com.atguigu.gmall.model.api.AttrValueJsonVO;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseCategoryViewService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@RestController
@RequestMapping("/api/product")
public class SkuDetailApiController {

    @Autowired
    private BaseCategoryViewService baseCategoryViewService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @GetMapping("/categoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable("category3Id") Long category3Id) {
        return baseCategoryViewService.getCategoryView(category3Id);
    }

    /**
     * 查询sku基本信息
     * @param skuId
     * @return
     */
    @GetMapping("/info/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        return skuInfo;
    }


    @GetMapping("/detail/price/{skuId}")
    public BigDecimal getSkuPriceById(@PathVariable("skuId") Long skuId) {
       return skuInfoService.getSkuPriceById(skuId);
    }

    @GetMapping("/image/{skuId}")
    public List<SkuImage> getSkuImageList(@PathVariable("skuId") Long skuId) {
        QueryWrapper<SkuImage> imageQueryWrapper = new QueryWrapper<>();
        imageQueryWrapper.eq("sku_id", skuId);
        List<SkuImage> skuImages = skuImageService.list(imageQueryWrapper);
        return skuImages;
    }

    @GetMapping("/saleattr/{spuId}/{skuId}")
    public List<SpuSaleAttr> getSpuSaleAttrBySpuIdAndSkuId(@PathVariable("spuId") Long spuId,
                                                           @PathVariable("skuId") Long skuId) {
        return spuSaleAttrService.getSpuSaleAttrBySpuIdAndSkuId(spuId, skuId);
    }


    @GetMapping("json/{spuId}")
    public List<AttrValueJsonVO> getAttrValueJsonVOList(@PathVariable("spuId") Long spuId) {
        return spuSaleAttrService.getAttrValueJsonVOList(spuId);
    }
}
