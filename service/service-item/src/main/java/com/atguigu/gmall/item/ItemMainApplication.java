package com.atguigu.gmall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/09
 */
@SpringCloudApplication
//basePackages引入依赖的模块中使用了@FeignClient注解的接口
@EnableFeignClients(basePackages = {"com.atguigu.gmall.product.client"})
public class ItemMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemMainApplication.class, args);
    }
}
