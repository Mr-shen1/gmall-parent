package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategoryView;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author SSS
* @description 针对表【base_category_view】的数据库操作Service
* @createDate 2021-12-10 21:23:48
*/
public interface BaseCategoryViewService extends IService<BaseCategoryView> {

    /**
     * 根据三级分类的id, 获取一级和二级分类信息
     * @param category3Id
     * @return
     */
    BaseCategoryView getCategoryView(Long category3Id);
}
