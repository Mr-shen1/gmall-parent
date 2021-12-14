package com.atguigu.gmall.product.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/13
 */
@SpringBootTest
public class ComponentTest {

    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void testRedissonClient() {
        System.out.println(redissonClient);

    }

}
