package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.api.AttrValueJsonVO;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {


    /**
     * 根据spuId获取销售属性值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);

    /**
     * 根据skuId和spuId获取销售属性
     * @param spuId
     * @param skuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrBySpuIdAndSkuId(@Param("spuId") Long spuId, @Param("skuId") Long skuId);

    /**
     * 根据spuId获取商品属性切换所需的信息
     * @param spuId
     * @return
     */
    List<AttrValueJsonVO> getAttrValueJsonVOList(@Param("spuId") Long spuId);
}
