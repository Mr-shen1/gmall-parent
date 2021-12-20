package com.atguigu.gmall.list;

import com.atguigu.gmall.list.service.impl.SearchServiceImpl;
import com.atguigu.gmall.model.list.SearchParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/20
 */
@SpringBootTest
public class EsSearchTest {

    @Autowired
    private SearchServiceImpl searchServiceImpl;

    @Test
    public void SearchServiceImplTest() throws Exception {

        SearchParam searchParam = new SearchParam();
        searchParam.setCategory3Id(61L);
        //searchParam.setTrademark("2:华为");
        //searchParam.setKeyword("荣耀");
        //searchParam.setOrder("");
        //searchParam.setProps(new String[]());
        //searchParam.setPageNo(0);
        //searchParam.setPageSize(0);

        searchServiceImpl.search(searchParam);

    }
}
