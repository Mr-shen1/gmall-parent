package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.item.service.IndexService;
import com.atguigu.gmall.model.api.CategoryVO;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@Service
public class ItemServiceImpl implements IndexService {

    @Autowired
    private ProductFeignClient productFeignClient;


    @Override
    public List<CategoryVO> getCategoryAll() {
        return productFeignClient.getCategoryAll();
    }
}
