package com.atguigu.gmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
@SpringCloudApplication
//@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
public class ProductMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductMainApplication.class, args);
    }
}
