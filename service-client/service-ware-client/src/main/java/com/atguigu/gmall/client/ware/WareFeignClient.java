package com.atguigu.gmall.client.ware;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@FeignClient(value = "ware-manage", url = "http://localhost:9001")
public interface WareFeignClient {


    @GetMapping("/hasStock")
    String hasStock(@RequestParam("skuId") Long skuId,
                    @RequestParam("num") Integer num);
}
