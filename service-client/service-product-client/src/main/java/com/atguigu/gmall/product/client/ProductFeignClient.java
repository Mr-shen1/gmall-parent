package com.atguigu.gmall.product.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@FeignClient("service-product")
public interface ProductFeignClient {



}
