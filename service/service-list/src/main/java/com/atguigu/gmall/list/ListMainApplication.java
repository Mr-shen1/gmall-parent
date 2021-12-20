package com.atguigu.gmall.list;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/17
 */
@SpringCloudApplication
@EnableElasticsearchRepositories
public class ListMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ListMainApplication.class, args);
    }
}
