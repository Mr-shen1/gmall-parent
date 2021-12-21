package com.atguigu.gmall.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/21
 */
@Component
@ConfigurationProperties(prefix = "auth-urls")
@Data
public class FilterProperties {

    private List<String> auth;
    private String loginUrl;

}
