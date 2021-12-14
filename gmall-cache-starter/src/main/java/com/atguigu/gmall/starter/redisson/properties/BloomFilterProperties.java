package com.atguigu.gmall.starter.redisson.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
@Data
@ConfigurationProperties(prefix = "service.bloom")
public class BloomFilterProperties {

    private Map<String, BloomProperty> config;


}
