package com.atguigu.gmall.order.config;

import com.atguigu.gmall.common.config.FeignCommonConfig;
import com.atguigu.gmall.common.config.MybatisPlusConfig;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@Configuration
@Import({
        MybatisPlusConfig.class,
        FeignCommonConfig.class
})
@EnableFeignClients(basePackages = {
        "com.atguigu.gmall.client.cart",
        "com.atguigu.gmall.client.product",
        "com.atguigu.gmall.client.user",
        "com.atguigu.gmall.client.ware"

})
public class OrderConfig {


}
