package com.atguigu.gmall.user.api;

import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@RestController
@RequestMapping("/api/inner/user")
public class UserApiController {

    @Autowired
    private UserAddressService userAddressService;


    @GetMapping("/useraddress")
    public List<UserAddress> getUserAddressList() {


       return userAddressService.getUserAddressList();
    }
}
