package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import com.atguigu.gmall.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author SSS
 * @description 针对表【user_info(用户表)】的数据库操作Service实现
 * @createDate 2021-12-21 12:50:11
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Map<String, Object> login(UserInfo userInfo, HttpServletRequest request) throws JsonProcessingException {

        String loginName = userInfo.getLoginName();
        String passwd = userInfo.getPasswd();

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", loginName);
        queryWrapper.eq("passwd", MD5.encrypt(passwd));
        UserInfo userInfoResult = baseMapper.selectOne(queryWrapper);

        if (userInfoResult == null) {
            // 查不到信息, 登录失败
            return null;
        }
        //
        Map<String, Object> map = token2Redis(request, userInfoResult);
        return map;
    }


    /**
     * 处理token信息
     * @param request
     * @param userInfoResult
     * @return
     * @throws JsonProcessingException
     */
    private Map<String, Object> token2Redis(HttpServletRequest request, UserInfo userInfoResult) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();

        map.put("nickName", userInfoResult.getNickName());

        String token = UUID.randomUUID().toString().replace("-", "") ;
        // 以token为key, 用户的id存入redis
        Map<Object, Object> map2Redis = new HashMap<>();
        map2Redis.put("ip", IpUtil.getIpAddress(request));
        map2Redis.put("userId", userInfoResult.getId());
        stringRedisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX + token,
                new ObjectMapper().writeValueAsString(map2Redis),
                RedisConst.USERKEY_TIMEOUT,
                TimeUnit.SECONDS);

        map.put("token", token);
        return map;
    }


    /**
     * 删除redis中的用户信息
     * @param token
     */
    @Override
    public void logout(String token) {

        String redisInfo = RedisConst.USER_LOGIN_KEY_PREFIX + token;
        stringRedisTemplate.delete(redisInfo);

    }
}




