package com.atguigu.gmall.starter.thread.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/15
 */
@Data
@ConfigurationProperties(prefix = "service.thread-pool")
public class ThreadPoolProperties {

    private int corePoolSize = 4;
    private int maximumPoolSize = 8;
    private long keepAliveTime = 60;
    private String name = "";


}
