package com.atguigu.gmall.cart.config;

import com.atguigu.gmall.common.config.FeignCommonConfig;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/22
 */
@Configuration
@EnableFeignClients(basePackages = {"com.atguigu.gmall.client.product"})
@Import(FeignCommonConfig.class)
public class CartConfig {



}
