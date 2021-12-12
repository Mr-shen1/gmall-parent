package com.atguigu.gmall.product.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/12
 */
@SpringBootTest
public class ProductTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test  //redis的value不允许null
    void test2(){
        stringRedisTemplate.opsForValue().set("aaa",null);
        System.out.println("保存成功....");
        String aaa = stringRedisTemplate.opsForValue().get("aaa");
        System.out.println("获取到的值："+aaa);
    }

    @Test
    void test3() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(null);
        System.out.println("null的json是什么？"+s.length());
    }
}
