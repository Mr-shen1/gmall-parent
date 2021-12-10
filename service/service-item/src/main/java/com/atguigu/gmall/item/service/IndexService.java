package com.atguigu.gmall.item.service;

import com.atguigu.gmall.model.api.CategoryVO;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
public interface IndexService {

    /**
     * 获取所有的分类信息
     *
     * @return
     */
    List<CategoryVO> getCategoryAll();
}
