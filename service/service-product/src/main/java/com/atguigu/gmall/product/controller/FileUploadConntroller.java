package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/07
 */
@RestController
@RequestMapping("/admin/product")
public class FileUploadConntroller {


    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("fileUpload")

    public Result uploadLogo(@RequestPart("file") MultipartFile file) {
        String path = fileUploadService.uploadLogo(file);
        return Result.ok(path);
    }
}

