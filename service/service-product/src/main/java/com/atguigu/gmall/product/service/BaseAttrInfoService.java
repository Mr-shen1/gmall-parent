package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    /**
     * 获取平台属性
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> getBaseAttr(Long category1Id, Long category2Id, Long category3Id);

    /**
     * 添加或修改平台属性
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);
}
