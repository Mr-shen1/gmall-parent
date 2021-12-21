package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.user.service.UserInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/21
 */
@RestController
@RequestMapping({"/api/user"})
public class UserLoginController {

    @Autowired
    private UserInfoService userInfoService;


    /**
     * 登录功能
     *
     * @param userInfo
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/passport/login")
    public Result login(@RequestBody UserInfo userInfo, HttpServletRequest request) throws JsonProcessingException {

        Map<String, Object> map = userInfoService.login(userInfo, request);
        if (map == null) {
            return Result.fail().message("用户名或密码输入错误");
        }

        return Result.ok(map);
    }

    /**
     * 登出功能, 由于前端会移除 Token 和用户信息, 并且跳转到首页, 所以我们只需要将 redis 中存的数据清除即可
     * auth.removeToken()
     * auth.removeUserInfo()
     * //跳转页面
     * window.location.href = "/"
     *
     * @return
     */
    @GetMapping("/passport/logout")
    public Result logout(@RequestHeader("token") String token) {
        userInfoService.logout(token);
        return Result.ok();
    }
}
