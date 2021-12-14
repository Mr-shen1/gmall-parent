package com.atguigu.gmall.common.config;

import com.atguigu.gmall.common.constant.RedisConst;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置信息
 */
@Configuration
public class RedissonAutoConfiguration {


    @Autowired
    private RedisProperties redisProperties;


    @Bean
    public RedissonClient getRedisClient() {

        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword());
        RedissonClient redissonClient = Redisson.create(config);

        return redissonClient;
    }

    @Bean
    public RBloomFilter<Object> skuBloomFilter() {

        //就算调一遍方法，也是用 redissonClient()之前创建的。
        //1、如果一个类上 标注了 @Configuration，SpringBoot知道这个类是个配置类
        //2、配置类在容器中是代理对象
        //3、调方法的时候redissonClient();相当于代理对象会对此方法进行拦截增强
        //4、@Bean的方法，任何时候调用逻辑：
        //    4.1、先看Spring容器中有没有 这个方法返回值类型的组件，如果有，用容器中的。
        //    4.2、如果容器中没有这个类型组件，这个方法就会运行创建出组件，并放在容器中
        RedissonClient redisClient = getRedisClient();
        RBloomFilter<Object> bloomFilter = redisClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);

        boolean b = bloomFilter.tryInit(1000000, 0.001);
        if (b) {

        } else {

        }

        return bloomFilter;
    }

}