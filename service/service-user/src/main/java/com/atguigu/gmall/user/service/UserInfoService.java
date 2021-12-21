package com.atguigu.gmall.user.service;

import com.atguigu.gmall.model.user.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @author SSS
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2021-12-21 12:50:11
*/
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 验证登录并且返回token和nickName
     * @param userInfo
     * @return
     */
    Map<String, Object> login(UserInfo userInfo, HttpServletRequest reques) throws JsonProcessingException;

    /**
     * 登出
     * @param token
     */
    void logout(String token);
}
