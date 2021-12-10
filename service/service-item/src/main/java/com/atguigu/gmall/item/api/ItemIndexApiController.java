package com.atguigu.gmall.item.api;

import com.atguigu.gmall.item.service.IndexService;
import com.atguigu.gmall.model.api.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@RestController
@RequestMapping("/api/item")
public class ItemIndexApiController {

    @Autowired
    private IndexService itemService;


    @GetMapping("/index/allCategory")
    public List<CategoryVO> getCategoryAll() {
        return itemService.getCategoryAll();
    }


}
