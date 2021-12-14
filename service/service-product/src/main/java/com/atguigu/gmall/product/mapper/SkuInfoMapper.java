package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author SSS
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2021-12-08 14:54:57
* @Entity com.atguigu.gmall.product.domain.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    /**
     * 根据skuId获取价格
     * @param skuId
     * @return
     */
    BigDecimal getSkuPriceById(@Param("skuId") Long skuId);

    List<String> selectAllIds();
}




