package com.atguigu.gmall.client.user;

import com.atguigu.gmall.model.user.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@FeignClient("service-user")
@RequestMapping("/api/inner/user")
public interface UserFeignClient {

    @GetMapping("/useraddress")
    List<UserAddress> getUserAddressList();


}
