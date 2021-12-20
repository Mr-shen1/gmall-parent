package com.atguigu.gmall.list.service;

import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/19
 */
public interface SearchService {


    SearchResponseVo search(SearchParam searchParam);
}
