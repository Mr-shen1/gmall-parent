package com.atguigu.gmall.starter.cache;

import com.atguigu.gmall.starter.cache.aspect.GmallCacheAspect;
import com.atguigu.gmall.starter.cache.core.DbCacheConsensusService;
import com.atguigu.gmall.starter.redisson.RedissonAutoConfiguration;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
@Configuration
@AutoConfigureAfter({RedisAutoConfiguration.class, RedissonAutoConfiguration.class})

public class CacheAutoConfiguration {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;


    @Bean
    public GmallCacheAspect getGmallCacheAspect() {


        return new GmallCacheAspect(redisTemplate, redissonClient);
    }

    @Bean
    public DbCacheConsensusService getDbCacheConsensusService() {
        DbCacheConsensusService dbCacheConsensusService = new DbCacheConsensusService(redisTemplate);

        return dbCacheConsensusService;
    }
}
