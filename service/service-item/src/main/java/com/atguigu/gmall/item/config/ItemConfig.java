package com.atguigu.gmall.item.config;

import com.atguigu.gmall.common.config.Swagger2Config;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@Configuration
//basePackages引入依赖的模块中使用了@FeignClient注解的接口
@EnableFeignClients(basePackages = {"com.atguigu.gmall.product.client"})
@Import({Swagger2Config.class})
public class ItemConfig {
}
