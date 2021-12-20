package com.atguigu.gmall.client.list;

import com.atguigu.gmall.model.list.SearchParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/19
 */
@FeignClient("service-list")
@RequestMapping("/api/es/search")
public interface SearchEsListFeignClient {


    @PostMapping("/sku")
    Map<String, Object> search(@RequestBody SearchParam searchParam);
}
