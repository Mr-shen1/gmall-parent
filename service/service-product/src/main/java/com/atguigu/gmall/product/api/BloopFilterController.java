package com.atguigu.gmall.product.api;

import com.atguigu.gmall.product.service.SkuInfoService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/13
 */
@RequestMapping("/api/bloom")
@RestController
public class BloopFilterController {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SkuInfoService skuInfoService;


    //skuBloomFilter
    @Qualifier("skuBloomFilter") //精确指定用哪个名字的组件
    @Autowired
    RBloomFilter<Object> skuBloomFilter;  //Spring5.0



    //    @GetMapping("/init")
    public String initBloom(){

        skuBloomFilter.tryInit(1000000,0.0001);

        return "ok：布隆初始化了";
    }


    /**
     * 模拟布隆添加数据
     */
//    @GetMapping("/add/{skuId}")
    public String addValueToBloom(@PathVariable("skuId") Long skuId){
        RBloomFilter<Long> filter = redissonClient.getBloomFilter("sku-bloom");
        filter.add(skuId);  //给数据库存东西的时候，给布隆放一份占位
        return skuId+"：添加完成";
    }

    /**
     * 模拟布隆判断
     * @param skuId
     * @return
     */
    @GetMapping("/exist/{skuId}")
    public String existKey(@PathVariable("skuId") Long skuId){
        RBloomFilter<Object> filter = redissonClient.getBloomFilter("sku-bloom");
        //只要布隆返回false，不用继续查库
        return filter.contains(skuId.toString()) + "==>数据库有没有";
    }


    /**
     * 布隆用久需要重建
     * 1、为了方便，放一个按钮，【重置布隆】
     */
    @GetMapping("/rebuild/{bloomName}")
    public String rebuildBloom(@PathVariable("bloomName") String bloomName){

        skuInfoService.rebuildBloomFilter();
        return bloomName + "：布隆重建完成";
    }


}
