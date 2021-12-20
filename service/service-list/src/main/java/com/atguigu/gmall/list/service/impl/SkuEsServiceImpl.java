package com.atguigu.gmall.list.service.impl;

import com.atguigu.gmall.list.repository.GoodsRepository;
import com.atguigu.gmall.list.service.SkuEsService;
import com.atguigu.gmall.model.list.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/18
 */
@Service
public class SkuEsServiceImpl implements SkuEsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public void skuEsService(Goods goods) {
        goodsRepository.save(goods);
    }

    @Override
    public void downSkuInfo(Long skuId) {

        goodsRepository.deleteById(skuId);
    }

    @Override
    public void incrScore(Long skuId, Long hotScore) {
        Optional<Goods> byId = goodsRepository.findById(skuId);
        Goods goods = byId.get();
        goods.setHotScore(hotScore);
        // 添加修改二合一
        goodsRepository.save(goods);
    }
}
