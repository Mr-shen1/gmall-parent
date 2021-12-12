package com.atguigu.gmall.product.api;

import com.atguigu.gmall.model.api.CategoryVO;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/09
 */
@RestController
@RequestMapping("/api/product")
public class ProductApiController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    @GetMapping("/index/allCategory")
    public List<CategoryVO> getCategoryAll() throws JsonProcessingException, InterruptedException {
        List<CategoryVO> categoryAll = baseCategory1Service.getCategoryAllByCache(null);
        //List<CategoryVO> categoryAll = baseCategory1Service.getAllCategoryFromDB();
        return categoryAll;
    }

}
