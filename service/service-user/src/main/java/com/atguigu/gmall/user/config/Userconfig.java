package com.atguigu.gmall.user.config;

import com.atguigu.gmall.common.config.MybatisPlusConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/21
 */
@Configuration
@Import({MybatisPlusConfig.class})
public class Userconfig {
}
