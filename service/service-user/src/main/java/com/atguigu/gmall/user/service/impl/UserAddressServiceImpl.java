package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.user.mapper.UserAddressMapper;
import com.atguigu.gmall.user.service.UserAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SSS
 * @description 针对表【user_address(用户地址表)】的数据库操作Service实现
 * @createDate 2021-12-24 20:14:28
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
        implements UserAddressService {

    @Override
    public List<UserAddress> getUserAddressList() {


        String userId = AuthContextHolder.getUserId();
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        // 转为Long类型  类型转换索引会失效
        queryWrapper.eq("user_id", Long.valueOf(userId));
        return baseMapper.selectList(queryWrapper);

    }
}




