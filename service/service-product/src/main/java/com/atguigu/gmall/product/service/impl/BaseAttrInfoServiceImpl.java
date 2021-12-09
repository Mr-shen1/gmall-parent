package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo> implements BaseAttrInfoService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;
    @Autowired
    private BaseAttrValueService baseAttrValueService;


    @Override
    public List<BaseAttrInfo> getBaseAttr(Long category1Id, Long category2Id, Long category3Id) {

        return baseAttrInfoMapper.getBaseAttr(category1Id, category2Id, category3Id);
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {

        if (baseAttrInfo.getId() == null) {
            // 添加
            insertAttrInfo(baseAttrInfo);
        }else {
            //修改
            updateAttrInfo(baseAttrInfo);
        }

    }

    private void updateAttrInfo(BaseAttrInfo baseAttrInfo) {
        //修改info
        this.updateById(baseAttrInfo);
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        //修改value
        List<BaseAttrValue> updateList = attrValueList.stream().filter((ele) -> ele.getAttrId() != null).collect(Collectors.toList());
        //批量修改value
        baseAttrValueService.updateBatchById(updateList);
        //删除value
        List<Long> deleteList = updateList.stream().map(BaseAttrValue::getId).collect(Collectors.toList());
        QueryWrapper<BaseAttrValue> deletewrapper = new QueryWrapper<>();
        deletewrapper.notIn("id", deleteList);
        deletewrapper.eq("attr_id", baseAttrInfo.getId());
        baseAttrValueMapper.delete(deletewrapper);
        //添加value
        List<BaseAttrValue> insertList = attrValueList.stream().filter((ele) -> ele.getAttrId() == null).map((ele) -> {
            ele.setAttrId(baseAttrInfo.getId());
            return ele;
        }).collect(Collectors.toList());
        // 批量添加
        baseAttrValueService.saveBatch(insertList);
    }

    private void insertAttrInfo(BaseAttrInfo baseAttrInfo) {
        // 1 先添加平台属性信息
        baseMapper.insert(baseAttrInfo);
        Long id = baseAttrInfo.getId();
        // 2 添加平台属性值
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

        //for (BaseAttrValue baseAttrValue : attrValueList) {
        //    baseAttrValue.setAttrId(id);
        //
        //    baseAttrValueMapper.insert(baseAttrValue);
        //}

        List<BaseAttrValue> collect = attrValueList.stream().map((ele) -> {
            ele.setAttrId(id);
            return ele;
        }).collect(Collectors.toList());

        //批量添加
        baseAttrValueService.saveBatch(collect);
    }
}