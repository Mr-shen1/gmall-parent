package com.atguigu.gmall.starter.cache.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/18
 */
@Slf4j
public class DbCacheConsensusService {

    private StringRedisTemplate redisTemplate;
    private ScheduledExecutorService scheduledExecutorService;

    public DbCacheConsensusService(StringRedisTemplate redisTemplate) {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 延迟双删
     *
     * @param key
     * @return
     */
    public boolean delayDoubleDelete(String key) {

        // 判断是否删除成功
        Boolean delete = redisTemplate.delete(key);
        try {
            scheduledExecutorService.schedule(() -> {
                redisTemplate.delete(key);
            }, 4, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("delayDoubleDelete() [缓存方法] called with exception => 【key = {}】", key, e);

        }
        return delete;
    }

}
