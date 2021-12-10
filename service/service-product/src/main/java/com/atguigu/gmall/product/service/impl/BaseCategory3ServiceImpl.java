package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
@Service
public class BaseCategory3ServiceImpl extends ServiceImpl<BaseCategory3Mapper, BaseCategory3> implements BaseCategory3Service {

    @Override
    public List<BaseCategory3> getCategory3By2Id(Long category2Id) {
        QueryWrapper<BaseCategory3> queryWrapper = new QueryWrapper<>();
        //SELECT * FROM `base_category3` where category2_id =
        queryWrapper.eq("category2_id", category2Id);
        return baseMapper.selectList(queryWrapper);
    }
}