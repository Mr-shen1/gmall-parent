package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
@RequestMapping("/admin/product")
@RestController
public class BaseCategoryController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    @Autowired
    private BaseCategory2Service baseCategory2Service;

    @Autowired
    private BaseCategory3Service baseCategory3Service;

    /**
     * 获取一级分类
     *
     * @return
     */
    @GetMapping("/getCategory1")
    public Result getBaseCategory() {
        List<BaseCategory1> list = baseCategory1Service.list();
        return Result.ok(list);
    }

    @GetMapping("/getCategory2/{category1Id}")
    public Result getCategory2By1Id(@PathVariable() Long category1Id) {
        List<BaseCategory2> list = baseCategory2Service.getCategory2By1Id(category1Id);
        return Result.ok(list);

    }

    @GetMapping("/getCategory3/{category2Id}")
    public Result getCategory3By2Id(@PathVariable() Long category2Id) {
        List<BaseCategory3> list = baseCategory3Service.getCategory3By2Id(category2Id);
        return Result.ok(list);

    }

}
