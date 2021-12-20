package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import com.atguigu.gmall.product.mapper.SpuSaleAttrValueMapper;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/07
 */
@Service
@Transactional
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo> implements SpuInfoService {

    @Autowired
    private SpuImageService spuImageService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;
    @Autowired
    private SpuSaleAttrValueService spuSaleAttrValueService;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public Page<SpuInfo> getSpuInfoPage(Long page, Long limit, Long category3Id) {


        Page<SpuInfo> pageInfo = new Page<>(page, limit);
        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id", category3Id);
        Page<SpuInfo> resultPage = baseMapper.selectPage(pageInfo, wrapper);

        return resultPage;
    }

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //1 添加SpuInfo信息
        baseMapper.insert(spuInfo);
        //获取自增主键的id
        Long spuId = spuInfo.getId();

        //2 添加SpuImage信息
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        List<SpuImage> imageList = spuImageList.stream().map((ele) -> {
            ele.setSpuId(spuId);
            return ele;
        }).collect(Collectors.toList());
        //批量添加图片
        spuImageService.saveBatch(imageList);

        //3 添加SpuSaleAttr信息
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        //saveAttrSaleValue(spuId, spuSaleAttrList);
        List<SpuSaleAttr> spuSaleAttrResultList = spuSaleAttrList.stream().map((ele) -> {
            ele.setSpuId(spuId);
            return ele;
        }).collect(Collectors.toList());
        spuSaleAttrService.saveBatch(spuSaleAttrResultList);

        //4 SpuSaleValue信息
        List<SpuSaleAttrValue> collect = spuSaleAttrList.stream().flatMap((ele) -> {
            return ele.getSpuSaleAttrValueList().stream().map((e1) -> {
                e1.setSpuId(spuId);
                e1.setSaleAttrName(ele.getSaleAttrName());
                return e1;
            });
        }).collect(Collectors.toList());
        spuSaleAttrValueService.saveBatch(collect);
    }

    private void saveAttrSaleValue(Long spuId, List<SpuSaleAttr> spuSaleAttrList) {
        spuSaleAttrList.stream().forEach((ssaele) -> {
            ssaele.setSpuId(spuId);
            // 3.1 添加SpuSaleAttr信息
            spuSaleAttrService.save(ssaele);

            List<SpuSaleAttrValue> spuSaleAttrValueList = ssaele.getSpuSaleAttrValueList();
            spuSaleAttrValueList.stream().forEach((ssavele) -> {
                //3.2 添加SpuSaleValue信息
                ssavele.setSpuId(ssaele.getSpuId());
                ssavele.setSaleAttrName(ssaele.getSaleAttrName());
                spuSaleAttrValueMapper.insert(ssavele);
            });
        });
    }
}