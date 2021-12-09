package com.atguigu.gmall.product.component;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/07
 */
@Configuration
@Import(MinioProperties.class)
@Slf4j
public class MinioAutoConfigruation {

    //@Autowired
    //private MinioProperties minioProperties;
    //
    @Bean
    public MinioClient createMinioClient(MinioProperties minioProperties) {
        String endpoint = minioProperties.getEndpoint();
        String accessKey = minioProperties.getAccessKey();
        String secretKey = minioProperties.getSecretKey();
        String bucketName = minioProperties.getBucketName();

        MinioClient minioClient = null;
        try {
            minioClient = new MinioClient(
                    endpoint,
                    accessKey,
                    secretKey);
            if (!minioClient.bucketExists(bucketName)) {
                minioClient.makeBucket(bucketName);
            }
        } catch (Exception e) {
            //e.printStackTrace()F;
            log.error("发生了异常", e);

        }
        return minioClient;}

}
