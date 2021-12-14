package com.atguigu.gmall.product;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
@SpringBootTest
public class GmallTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RBloomFilter rBloomFilter;

    
    @Test
    public void testGmallStarter() {
        System.out.println(redissonClient);

    }
}
