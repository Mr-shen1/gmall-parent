package com.atguigu.gmall.item.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@Configuration
//basePackages引入依赖的模块中使用了@FeignClient注解的接口
@EnableFeignClients(basePackages = {"com.atguigu.gmall.product.client"})
public class ItemConfig {
}
