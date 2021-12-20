package com.atguigu.gmall.starter.redisson;

import com.atguigu.gmall.starter.redisson.properties.BloomFilterProperties;
import com.atguigu.gmall.starter.redisson.properties.BloomProperty;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
@EnableConfigurationProperties(BloomFilterProperties.class)
@Slf4j
public class RedissonAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    //@Autowired
    //private BloomFilterProperties bloomFilterProperties;

    @ConditionalOnMissingBean // 容器中没有这个bean才生效  允许自定义
    @Bean
    public RedissonClient getRedisClient() {

        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword());
        RedissonClient redissonClient = Redisson.create(config);

        //log.error("RedissonClient ,{}, 获取到了", redissonClient);
        return redissonClient;
    }

    @Bean("skuBloomFilter")
    public RBloomFilter<Object> skuBloomFilter(BloomFilterProperties bloomFilterProperties) {

        //就算调一遍方法，也是用 redissonClient()之前创建的。
        //1、如果一个类上 标注了 @Configuration，SpringBoot知道这个类是个配置类
        //2、配置类在容器中是代理对象
        //3、调方法的时候redissonClient();相当于代理对象会对此方法进行拦截增强
        //4、@Bean的方法，任何时候调用逻辑：
        //    4.1、先看Spring容器中有没有 这个方法返回值类型的组件，如果有，用容器中的。
        //    4.2、如果容器中没有这个类型组件，这个方法就会运行创建出组件，并放在容器中
        RedissonClient redisClient = getRedisClient();

        //获取sku布隆过滤器的配置
        BloomProperty skuBloomProperty = bloomFilterProperties.getConfig().get("sku");
        RBloomFilter<Object> bloomFilter = redisClient.getBloomFilter(skuBloomProperty.getBloomName());

        boolean b = bloomFilter.tryInit(skuBloomProperty.getExpectedInsertions(), skuBloomProperty.getFalseProbability());
        //log.error("skuBloomFilter ,{}, 获取到了", bloomFilter);
        return bloomFilter;
    }

    //@ConditionalOnMissingBean
    @Bean("spuBloomFilter")
    public RBloomFilter<Object> spuBloomFilter(BloomFilterProperties bloomFilterProperties) {

        //就算调一遍方法，也是用 redissonClient()之前创建的。
        //1、如果一个类上 标注了 @Configuration，SpringBoot知道这个类是个配置类
        //2、配置类在容器中是代理对象
        //3、调方法的时候redissonClient();相当于代理对象会对此方法进行拦截增强
        //4、@Bean的方法，任何时候调用逻辑：
        //    4.1、先看Spring容器中有没有 这个方法返回值类型的组件，如果有，用容器中的。
        //    4.2、如果容器中没有这个类型组件，这个方法就会运行创建出组件，并放在容器中
        RedissonClient redisClient = getRedisClient();

        //获取sku布隆过滤器的配置
        BloomProperty skuBloomProperty = bloomFilterProperties.getConfig().get("spu");
        RBloomFilter<Object> bloomFilter = redisClient.getBloomFilter(skuBloomProperty.getBloomName());

        boolean b = bloomFilter.tryInit(skuBloomProperty.getExpectedInsertions(), skuBloomProperty.getFalseProbability());


        //log.error("spuBloomFilter ,{}, 获取到了", bloomFilter);
        return bloomFilter;
    }
}
