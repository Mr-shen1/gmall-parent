package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.product.component.MinioProperties;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/07
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    /**
     * 获取MinioAutoConfigruation中的minioClient对象
     */
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String uploadLogo(MultipartFile file) {


        try {
            //获取文件名
            String fileName = UUID.randomUUID().toString().replace("-", "") + "_" +
                    file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            PutObjectOptions options = new PutObjectOptions(inputStream.available(), -1);
            options.setContentType(file.getContentType());
            //上传logo
            minioClient.putObject(minioProperties.getBucketName(),
                    fileName,
                    inputStream,
                    options);
            String path = minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + fileName;
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}