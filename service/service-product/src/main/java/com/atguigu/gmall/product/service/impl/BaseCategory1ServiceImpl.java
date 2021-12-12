package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.model.api.CategoryVO;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/06
 */
@Service
public class BaseCategory1ServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1> implements BaseCategory1Service {

    //public static final String CATEGORY_ALL = "categoryAll";
    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public List<CategoryVO> getCategoryAllByCache(String token) throws JsonProcessingException, InterruptedException {


        //return getCategoryMine(token);

        return getCategoryTeacher(token);


    }

    @Override
    public List<CategoryVO> getAllCategoryFromDB() throws JsonProcessingException {
        return baseCategory1Mapper.getCategoryAll();

    }

    private List<CategoryVO> getCategoryMine(String token) throws JsonProcessingException, InterruptedException {
        String cacheKey = RedisConst.INDEX_CACHE_PREFIX + RedisConst.CATEGORY;
        //String lockValue = UUID.randomUUID().toString();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isEmpty(json)) {// 缓存中没有
        String lockKey = RedisConst.LOCK_PREFIX + RedisConst.CATEGORY;
            String lockToken = "";
            Boolean locked = false;

            if (!StringUtils.isEmpty(token)) {
                // 此token为传来的
                lockToken = token;
                locked = true;
            }else{
                lockToken = UUID.randomUUID().toString();
            }

            if (!locked) {

                // 加分布式锁
                locked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockToken, 60, TimeUnit.MINUTES);

            }

            if (locked) {
                try {
                    // 抢到锁了
                    // 再次判断缓存中是否已经有数据
                    json = redisTemplate.opsForValue().get(cacheKey);
                    if (StringUtils.isEmpty(json)) {
                        //查询数据库
                        List<CategoryVO> categoryAll = getAllCategoryFromDB();
                        String redisValue = objectMapper.writeValueAsString(categoryAll);
                        // 存入缓存中
                        redisTemplate.opsForValue().set(cacheKey, redisValue);
                        // 返回
                        return categoryAll;
                    } else {
                        List<CategoryVO> vos = objectMapper.readValue(json, new TypeReference<List<CategoryVO>>() {
                        });
                        return vos;
                    }
                } finally {
                    //// 释放前判断锁还是否存在
                    //String lockExists = redisTemplate.opsForValue().get(lockKey);
                    //if (StringUtils.isEmpty(lockExists)) {
                        // 释放锁 释放锁要保证原子性 使用lua脚本
                        String script =
                                "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                        redisTemplate.execute(new DefaultRedisScript<Integer>(script), Arrays.asList(lockKey), lockToken);
                    //}
                }
            } else {
                try {
                    Boolean abset = redisTemplate.opsForValue().setIfAbsent(lockKey, lockToken, 60, TimeUnit.MINUTES);
                    while (!abset) {
                        TimeUnit.MILLISECONDS.sleep(30);
                        abset = redisTemplate.opsForValue().setIfAbsent(lockKey, lockToken, 60, TimeUnit.MINUTES);
                    }
                        //抢到锁则再次调用方法 //递归
                    List<CategoryVO> vos = getCategoryAllByCache(lockToken);
                    return vos;
                } finally {
                    // 释放前判断锁还是否存在
                    String lockExists = redisTemplate.opsForValue().get(lockKey);
                    if (StringUtils.isEmpty(lockExists)) {
                        // 释放锁 释放锁要保证原子性 使用lua脚本
                        String script =
                                "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                        redisTemplate.execute(new DefaultRedisScript<Integer>(script), Arrays.asList(lockKey), lockToken);
                    }


                }
            }

        } else {
            // 缓存中有, 直接返回
            // TypeReference 是抽象类
            List<CategoryVO> vos = objectMapper.readValue(json, new TypeReference<List<CategoryVO>>() {
            });
            return vos;
        }
    }

    private List<CategoryVO> getCategoryTeacher(String token) throws JsonProcessingException, InterruptedException {
        //1、先看缓存中有没有
        String cacheKey = RedisConst.INDEX_CACHE_PREFIX + RedisConst.CATEGORY;
        ObjectMapper objectMapper = new ObjectMapper();

        String json = redisTemplate.opsForValue().get(cacheKey);
        if(StringUtils.isEmpty(json) || "null".equals(json)){
            //2、缓存中没有，准备回源
            //2.1、回源前的准备：加分布式锁，锁的粒度就精确到业务
            String lockKey = RedisConst.LOCK_PREFIX + RedisConst.CATEGORY; //lock:category
            String lockToken = "";
            Boolean locked = false;
            if(!StringUtils.isEmpty(token)){
                //前面传来的。
                lockToken = token;
                locked = true;
            }else {
                lockToken = UUID.randomUUID().toString();
            }
            if(!locked){
                //没加过锁，才需要加锁
                locked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockToken, 60L, TimeUnit.MINUTES);
            }
            if(locked){
                try{
                    //2.2、成功拿到锁
                    //2.3、再准备查询缓存数据
                    json =  redisTemplate.opsForValue().get(cacheKey);
                    if(StringUtils.isEmpty(json)){
                        //2.4 缓存没有，查数据库
                        List<CategoryVO> category = getAllCategoryFromDB();
                        //2.4.1、查到数据放缓存
                        String redisJson = objectMapper.writeValueAsString(category);
                        //2.4.2、数据保存到redis
                        redisTemplate.opsForValue().set(cacheKey,redisJson,RedisConst.INDEX_CACHE_TIMEOUT,TimeUnit.SECONDS);
                        return category;
                    }else {
                        //2.5 缓存有，直接返回
                        List<CategoryVO> vos = objectMapper.readValue(json, new TypeReference<List<CategoryVO>>() {
                        });
                        return vos;
                    }
                }finally {
                    //解锁
                    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    redisTemplate.execute(new DefaultRedisScript<Integer>(script),Arrays.asList(lockKey),lockToken);
                }
            }else {
                //2.3、没有拿到锁。不断重试获取锁
                Boolean absent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockToken, 60L, TimeUnit.MINUTES);
                while (!absent){
                    TimeUnit.MILLISECONDS.sleep(30);
                    absent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockToken, 60L, TimeUnit.MINUTES);
                }
                //抢到了，方法调用一遍
                try {
                    List<CategoryVO> allVos = getCategoryAllByCache(lockToken); //递归,需要设计为可重入锁
                    return allVos;
                }finally {
                    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    redisTemplate.execute(new DefaultRedisScript<Integer>(script),Arrays.asList(lockKey),lockToken);
                }
            }
        }else {
            //3、缓存中有
            List<CategoryVO> vos = objectMapper.readValue(json, new TypeReference<List<CategoryVO>>() {
            });
            return vos;
        }
    }


    //// 添加本地缓存
    //private Map<String, List<CategoryVO>> cacheMap = new HashMap<>();
    //
    //@Override
    //public List<CategoryVO> getCategoryAll() {
    //
    //    List<CategoryVO> categoryAllInCache = cacheMap.get(CATEGORY_ALL);
    //    if (categoryAllInCache == null) {
    //        // 从数据库中查
    //        categoryAllInCache = baseCategory1Mapper.getCategoryAll();
    //        cacheMap.put(CATEGORY_ALL, categoryAllInCache);
    //        return categoryAllInCache;
    //    }
    //    return categoryAllInCache;
    //
    //}
}
