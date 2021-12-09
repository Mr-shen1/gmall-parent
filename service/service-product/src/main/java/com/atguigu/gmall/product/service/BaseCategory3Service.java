package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategory3;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
public interface BaseCategory3Service extends IService<BaseCategory3> {


    /**
     * 根据二级分类的id获取三级分类
     * @param category2Id
     * @return
     */
    List<BaseCategory3> getCategory3By2Id(Long category2Id);
}
