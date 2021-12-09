package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategory2;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
public interface BaseCategory2Service extends IService<BaseCategory2> {


    /**
     * 根据一级分类的id获取二级分类
     * @param category1Id
     * @return
     */
    List<BaseCategory2> getCategory2By1Id(Long category1Id);
}
