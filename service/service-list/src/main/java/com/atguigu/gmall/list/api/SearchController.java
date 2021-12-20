package com.atguigu.gmall.list.api;

import com.atguigu.gmall.list.service.SearchService;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/19
 */
@RestController
@RequestMapping("/api/es/search")
public class SearchController {

    @Autowired
    private SearchService searchService;


    @PostMapping("/sku")
    public SearchResponseVo search(@RequestBody SearchParam searchParam) {
        return searchService.search(searchParam);

    }
}
