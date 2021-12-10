package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseCategoryView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author SSS
* @description 针对表【base_category_view】的数据库操作Mapper
* @createDate 2021-12-10 21:23:48
* @Entity com.atguigu.gmall.product.domain.BaseCategoryView
*/
public interface BaseCategoryViewMapper extends BaseMapper<BaseCategoryView> {

    /**
     * 根据三级分类的id, 获取一级和二级分类信息
     * @param category3Id
     * @return
     */
    BaseCategoryView getCategoryView(@Param("category3Id") Long category3Id);
}




