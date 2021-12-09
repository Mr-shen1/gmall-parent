package com.atguigu.gmall.product;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.MimeTypeUtils;

import java.io.FileInputStream;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/07
 */
@SpringBootTest
public class MinioTest {


    public static final String BUCKET_NAME = "hello";

    @Test
    public void testMinio() throws Exception {

        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = new MinioClient(
                "http://192.168.112.130:9000",
                "mall",
                "mall123456");
        if (!minioClient.bucketExists(BUCKET_NAME)) {
            minioClient.makeBucket(BUCKET_NAME);
        }
        //System.out.println(minioClient);
        /**
         * String bucketName,
         * String objectName,
         * InputStream stream,
         * PutObjectOptions options
         *
         */
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\SSS\\Desktop\\Snipaste_2021-12-07_16-42-42.png");
        PutObjectOptions putObjectOptions = new PutObjectOptions(fileInputStream.available(), -1);
        putObjectOptions.setContentType(MimeTypeUtils.IMAGE_PNG.toString());

        minioClient.putObject(BUCKET_NAME,
                "Snipaste_2021-12-07_16-42-42.png",
                fileInputStream,
                putObjectOptions);

        System.out.println("上传成功");
    }

}
