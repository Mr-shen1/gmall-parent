package com.atguigu.gmall.order.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@Component
@ConfigurationProperties("service.order")
@Data
public class OrderProperties {

    private Long timeout;

}
