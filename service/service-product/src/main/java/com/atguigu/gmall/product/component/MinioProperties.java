package com.atguigu.gmall.product.component;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/07
 */
@Data
@ConfigurationProperties(prefix = "service.minio")
public class MinioProperties {


    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;


}
