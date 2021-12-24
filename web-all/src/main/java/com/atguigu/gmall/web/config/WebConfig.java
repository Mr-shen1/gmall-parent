package com.atguigu.gmall.web.config;

import com.atguigu.gmall.common.config.FeignCommonConfig;
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
@EnableFeignClients(basePackages = {"com.atguigu.gmall.client.item",
                                    "com.atguigu.gmall.client.list",
                                    "com.atguigu.gmall.client.cart",
                                    "com.atguigu.gmall.client.order",})
@Import(FeignCommonConfig.class)
public class WebConfig {




}
