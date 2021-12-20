package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.client.list.SearchEsListFeignClient;
import com.atguigu.gmall.model.list.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/17
 */
@Controller
public class ListController {

    @Autowired
    private SearchEsListFeignClient searchEsListFeignClient;


    @GetMapping("list.html")
    public String list(SearchParam searchParam, ModelMap modelMap) {
        Map<String, Object> search = searchEsListFeignClient.search(searchParam);

        modelMap.addAllAttributes(search);

        modelMap.addAttribute("searchParam", searchParam);

        //urlParam: 保存以前的访问路径，并且根据这次检索动态拼接出url的真正内容
        String url = makeUrlParam(searchParam);
        modelMap.addAttribute("urlParam", url);

        // 品牌面包屑
        String trademarkParam = makeTrademarkParam(searchParam);
        modelMap.addAttribute("trademarkParam", trademarkParam);

        // 平台属性面包屑
        // props=23:8G:运行内存
        String[] props = searchParam.getProps();
        List<Map<String, Object>> propsParamList = new ArrayList<>();

        if (props != null && props.length > 0) {
            for (String prop : props) {
                Map<String, Object> map = new HashMap<>();
                String[] split = prop.split(":");
                if (split != null && split.length > 0) {
                    map.put("attrId", split[0]);
                    map.put("attrValue", split[1]);
                    map.put("attrName", split[2]);
                }
                propsParamList.add(map);
            }
        }
        modelMap.addAttribute("propsParamList", propsParamList);

        //构造orderMap
        HashMap<Object, Object> map = new HashMap<>();

        if (!StringUtils.isEmpty(searchParam.getOrder())) {
            map.put("type", searchParam.getOrder().split(":")[0]);
            map.put("sort", searchParam.getOrder().split(":")[1]);
        } else {
            map.put("type", 1);
            map.put("sort", "desc");
        }
        modelMap.addAttribute("orderMap", map);


        return "list/index";
    }

    /**
     * 构建品牌面包屑
     *
     * @param searchParam
     * @return
     */
    private String makeTrademarkParam(SearchParam searchParam) {
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            if (split != null && split.length > 0) {
                trademark = "品牌:" + split[1];

            }
            return trademark;
        }
        return null;
    }


    private String makeUrlParam(SearchParam param) {
        StringBuilder base = new StringBuilder("list.html?");
        if (!StringUtils.isEmpty(param.getTrademark())) {
            //trademark=1:小米
            base.append("&trademark=" + param.getTrademark());
        }

        if (param.getCategory3Id() != null) {
            base.append("&category3Id=" + param.getCategory3Id());
        }

        if (!StringUtils.isEmpty(param.getKeyword())) {
            base.append("&keyword=" + param.getKeyword());
        }
        if (param.getProps() != null && param.getProps().length > 0) {
            for (String prop : param.getProps()) {
                base.append("&props=" + prop);
            }
        }

        base.replace(10, 11, "");
        return base.toString();
    }
}
