package com.atguigu.gmall.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/07
 */
public interface FileUploadService {

    /**
     * 上传Logo
     * @param file
     * @return: 文件路径
     */
    String uploadLogo(MultipartFile file);
}
