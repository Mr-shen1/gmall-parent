package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.api.AttrValueJsonVO;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr> implements SpuSaleAttrService {

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {


        return spuSaleAttrMapper.getSpuSaleAttrList(spuId);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrBySpuIdAndSkuId(Long spuId, Long skuId) {
        return spuSaleAttrMapper.getSpuSaleAttrBySpuIdAndSkuId(spuId, skuId);

    }

    @Override
    public List<AttrValueJsonVO> getAttrValueJsonVOList(Long spuId) {

        return spuSaleAttrMapper.getAttrValueJsonVOList(spuId);
    }
}