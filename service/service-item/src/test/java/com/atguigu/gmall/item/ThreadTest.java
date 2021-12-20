package com.atguigu.gmall.item;

import com.atguigu.gmall.client.product.ProductFeignClient;
import com.atguigu.gmall.model.api.AttrValueJsonVO;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/15
 */
@SpringBootTest
@Slf4j
public class ThreadTest {


    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Test
    @DisplayName("itemthreadPoolCreateTest")
    public void itemthreadPoolCreateTest() throws Exception {
        executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.error("itemthreadPoolCreateTest() called with exception => ",e);
            }

            System.out.println("hello");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.error("itemthreadPoolCreateTest() called with exception => ",e);
            }

            System.out.println("world");
        });

        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("itemthreadPoolCreateTest() called with exception => ",e);
        }
        System.out.println("main");

    }

    @Test
    @DisplayName("asyncScheduleTest")
    public void asyncScheduleTest() throws ExecutionException, InterruptedException {

        log.info("asyncScheduleTest()");
        long skuId = 46;

        Map<String, Object> map = new HashMap<>();

        // 异步查询skuInfo信息
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
            map.put("skuInfo", skuInfo);
            return skuInfo;
        }, executor);

        // 查询skuInfo中的图片信息
        CompletableFuture<Void> skuImageFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            List<SkuImage> skuImageList = productFeignClient.getSkuImageList(skuId);
            skuInfo.setSkuImageList(skuImageList);
        }, executor);

        CompletableFuture<BigDecimal> priceFuture = CompletableFuture.supplyAsync(() -> {

            BigDecimal price = productFeignClient.getSkuPriceById(skuId);
            map.put("price", price);
            return price;
        }, executor);


        CompletableFuture<BaseCategoryView> categoryViewFuture = skuInfoFuture.thenApplyAsync(skuInfo -> {
            BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
            map.put("categoryView", categoryView);
            return categoryView;
        }, executor);

        //获取销售属性列表
        CompletableFuture<List<SpuSaleAttr>> spuSaleAttrFuture = skuInfoFuture.thenApplyAsync(skuInfo -> {
            List<SpuSaleAttr> spuSaleAttr = productFeignClient.getSpuSaleAttrBySpuIdAndSkuId(skuInfo.getSpuId(), skuId);
            map.put("spuSaleAttrList", spuSaleAttr);
            return spuSaleAttr;
        }, executor);

        //获取销售属性切换所需的信息

        CompletableFuture<List<AttrValueJsonVO>> jsonResultFuture = skuInfoFuture.thenApplyAsync(skuInfo -> {
            List<AttrValueJsonVO> jsonResult = productFeignClient.getAttrValueJsonVOList(skuInfo.getSpuId());

            map.put("valuesSkuJson", jsonResult);
            return jsonResult;
        }, executor);

        CompletableFuture.allOf(skuInfoFuture,
                skuImageFuture,
                jsonResultFuture,
                spuSaleAttrFuture,
                categoryViewFuture,
                priceFuture).join();

        SkuInfo skuInfo = skuInfoFuture.get();
        System.out.println(skuInfo);

        System.out.println(map);

    }
}
