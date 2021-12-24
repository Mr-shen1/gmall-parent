package com.atguigu.gmall.user.service;

import com.atguigu.gmall.model.user.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author SSS
* @description 针对表【user_address(用户地址表)】的数据库操作Service
* @createDate 2021-12-24 20:14:28
*/
public interface UserAddressService extends IService<UserAddress> {


    /**
     * 根据用户id获取用户的所有地址
     * @return
     */
    List<UserAddress> getUserAddressList();
}
