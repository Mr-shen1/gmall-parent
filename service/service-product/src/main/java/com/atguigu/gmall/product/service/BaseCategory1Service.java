package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.api.CategoryVO;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
public interface BaseCategory1Service extends IService<BaseCategory1> {

    /**
     * 获取所有的分类信息
     * @return
     */
    List<CategoryVO> getCategoryAll();
}
