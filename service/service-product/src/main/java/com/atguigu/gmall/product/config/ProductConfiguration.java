package com.atguigu.gmall.product.config;

import com.atguigu.gmall.common.config.MybatisPlusConfig;
import com.atguigu.gmall.common.config.Swagger2Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({Swagger2Config.class, MybatisPlusConfig.class})
public class ProductConfiguration {


}
