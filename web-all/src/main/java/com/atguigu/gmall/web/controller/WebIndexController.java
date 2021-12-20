package com.atguigu.gmall.web.controller;


import com.atguigu.gmall.client.item.ItemFeignClient;
import com.atguigu.gmall.model.api.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@Controller
public class WebIndexController {

    @Autowired
    private ItemFeignClient itemFeignClient;

    @GetMapping("/")
    public String index(ModelMap modelMap) {
        List<CategoryVO> categoryVOList = itemFeignClient.getCategoryAll();
        modelMap.addAttribute("list", categoryVOList);
        return "index/index";
    }
}
