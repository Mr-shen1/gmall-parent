package com.atguigu.gmall.product.config;

import com.atguigu.gmall.common.config.MybatisPlusConfig;
import com.atguigu.gmall.common.config.Swagger2Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableScheduling
@Import({Swagger2Config.class, MybatisPlusConfig.class})
public class ProductConfiguration {


}
