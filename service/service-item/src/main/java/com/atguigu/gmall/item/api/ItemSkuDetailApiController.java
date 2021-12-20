package com.atguigu.gmall.item.api;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.client.list.SkuEsListFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@RestController
@RequestMapping("/api/item")
public class ItemSkuDetailApiController {

    @Autowired
    private SkuDetailService skuDetailService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SkuEsListFeignClient skuEsListFeignClient;

    @GetMapping("/sku/detail/{skuId}")
    Map<String, Object> skuDetail(@PathVariable("skuId") Long skuId) throws Exception {
        //Map<String, Object> data = skuDetailService.getSkuDeatailFromCache(skuId);
        //Map<String, Object> data = skuDetailService.getSkuDeatail(skuId);
        Map<String, Object> data = skuDetailService.getSkuDeatailAsync(skuId);

        // 更新热度分 没访问一次商品详情, 该商品的热度分就加一
        // 为了在高并发的情况下, 减小es的压力 先在redis中统计, 每满100分, 才往es中更新一次

        // 返回值为增加后的结果
        Long increment = redisTemplate.opsForHash().increment(RedisConst.SKU_HOT_SCORE, skuId.toString(), 1L);//即binary

        // 每一百次存入一次
        if (increment % 100 == 0) {
            skuEsListFeignClient.incrScore(skuId, increment);
        }
        return data;

    }
}
