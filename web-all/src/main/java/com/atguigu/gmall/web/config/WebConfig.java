package com.atguigu.gmall.web.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@Configuration
@EnableFeignClients(basePackages = {"com.atguigu.gmall.item.client"})
public class WebConfig {


}
