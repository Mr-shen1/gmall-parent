package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {


    /**
     * 获取平台属性
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> getBaseAttr(@Param("category1Id") Long category1Id,
                                   @Param("category2Id") Long category2Id,
                                   @Param("category3Id") Long category3Id);
}
