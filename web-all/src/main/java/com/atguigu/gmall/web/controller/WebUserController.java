package com.atguigu.gmall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/21
 */
@Controller
public class WebUserController {


    @GetMapping("/login.html")
    public String login(HttpServletRequest request, ModelMap modelMap) {
        String queryString = request.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            // 跳回原来的页面
            String resultQueryStr = queryString.substring(10);
            modelMap.addAttribute("originUrl", resultQueryStr);
        } else {
            // 如果没有查询字符串, 登录完成后返回首页
            modelMap.addAttribute("originUrl", "http://gmall.com");
        }

        return "login";

    }
}
