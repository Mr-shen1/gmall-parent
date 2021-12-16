package com.atguigu.gmall.starter.cache.aspect;

import com.atguigu.gmall.starter.cache.annotation.BizLockOptions;
import com.atguigu.gmall.starter.cache.annotation.BloomOptions;
import com.atguigu.gmall.starter.cache.annotation.GmallCache;
import com.atguigu.gmall.starter.cache.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
@Slf4j
@Aspect
public class GmallCacheAspect {


    public static final String LOCK_SUFFIX = ":lock";

    private StringRedisTemplate redisTemplate;

    private RedissonClient redissonClient;

    public GmallCacheAspect(StringRedisTemplate redisTemplate, RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.redisTemplate = redisTemplate;
    }

    // 基于注解实现切入点, 切入标注了该注解的方法
    @Around("@annotation(com.atguigu.gmall.starter.cache.annotation.GmallCache)")
    public Object aroundCache(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        // 获取目标对象的方法签名
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();


        // 获取cacheKey
        String cacheKey = parseException(proceedingJoinPoint);
        // 获取lockKey
        String lockKey = cacheKey + LOCK_SUFFIX;
        // 获取bloomName
        String bloomName = getBloomKName(proceedingJoinPoint);


        RLock lock = null;
        Object result = null;
        //1 确定缓存中是否存在
        String cacheKeyResult = redisTemplate.opsForValue().get(cacheKey);
        if (cacheKeyResult == null) {
            // 缓存中没有

            // 检测程序员是否添加了布隆
            BloomOptions bloomOptions = getBloomOptions(proceedingJoinPoint);
            // 注解没有添加属性的话, 名字默认为""
            if (!Objects.equals(bloomOptions.bloomName(), "")) {

                // 获取布隆要查的内容
                String checked = getBloomExp(proceedingJoinPoint);

                // 创建布隆并查询, 查布隆
                RBloomFilter<Object> bloom = redissonClient.getBloomFilter(bloomName);

                if (!bloom.contains(checked)) {
                    //布隆中没有
                    return null;
                }

            }

            //  程序员没有配布隆, 获取布隆中存在
            boolean locked = false;
            try {
                lock = redissonClient.getLock(lockKey);


                // 获取锁的信息
                BizLockOptions lockOptions = getLockOptions(proceedingJoinPoint);
                // 加锁
                locked = lock.tryLock(lockOptions.lockTime(), lockOptions.lockReleaseTime(), TimeUnit.SECONDS);

                if (!locked) {
                    // 没抢到锁, 查缓存返回
                    cacheKeyResult = redisTemplate.opsForValue().get(cacheKey);
                    return JsonUtils.convertToData(methodSignature, cacheKeyResult);
                }
                // 抢到锁了
                //再查一次缓存
                cacheKeyResult = redisTemplate.opsForValue().get(cacheKey);
                if (cacheKeyResult == null) {
                    // 查数据库
                    // 目标对象的方法执行即查询数据库
                    result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
                    // 将查出的数据放入缓存中
                    redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(result));

                    return result;

                } else {
                    return JsonUtils.convertToData(methodSignature, cacheKeyResult);
                }

            } catch (Throwable e) {
                log.error("方法aroundCache发生了异常", e);
                throw new RuntimeException(e);
            } finally {

                // 如果有锁, 则解锁
                if (locked) {
                    try {
                        lock.unlock();
                    } catch (Exception e) {
                        log.error("aroundCache() called with exception => 【proceedingJoinPoint = {}】",proceedingJoinPoint,e);
                    }

                }
            }
        } else {
            //缓存中有, 返回
            return JsonUtils.convertToData(methodSignature, cacheKeyResult);
        }
    }

    /**
     * 获取布隆信息
     *
     * @param proceedingJoinPoint
     * @return
     */
    BloomOptions getBloomOptions(ProceedingJoinPoint proceedingJoinPoint) {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        //1、获取当前目标方法的@GmallCache
        GmallCache cache = AnnotationUtils.findAnnotation(method, GmallCache.class);

        //得到布隆设置信息
        return cache.bloomOptions();
    }


    /**
     * 获取布隆要检查的值
     *
     * @param proceedingJoinPoint
     * @return
     */
    private String getBloomExp(ProceedingJoinPoint proceedingJoinPoint) {

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        GmallCache annotation = AnnotationUtils.findAnnotation(method, GmallCache.class);
        // 获取布隆要查的值
        String exp = annotation.bloomOptions().bloomExp();

        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(exp, new TemplateParserContext());

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args", proceedingJoinPoint.getArgs());

        return expression.getValue(context, String.class);
    }


    /**
     * 获取锁的信息
     *
     * @param proceedingJoinPoint
     * @return
     */
    private BizLockOptions getLockOptions(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache gmallCache = AnnotationUtils.findAnnotation(method, GmallCache.class);
        return gmallCache.lockOptions();

    }


    /**
     * 获取bloomKey
     *
     * @param proceedingJoinPoint
     * @return
     */
    private String getBloomKName(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache annotation = AnnotationUtils.findAnnotation(method, GmallCache.class);
        return annotation.bloomOptions().bloomName();


    }


    /**
     * 解析表达式 cacheKey
     *
     * @param proceedingJoinPoint
     * @return
     */
    private String parseException(ProceedingJoinPoint proceedingJoinPoint) {
        // 获取目标对象的方法签名
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 获取方法的参数
        Object[] args = proceedingJoinPoint.getArgs();
        // 获取目标方法
        Method method = methodSignature.getMethod();
        // 拿到目标方法标注的注解
        //GmallCache cache = method.getDeclaredAnnotation(GmallCache.class);
        // AnnotationUtils工具类
        GmallCache cache = AnnotationUtils.findAnnotation(method, GmallCache.class);
        // 获取方法的cacheKey
        String cacheKey = cache.cacheKey();


        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(cacheKey, new TemplateParserContext());

        // 计算环境
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args", args);


        return expression.getValue(context, String.class);

    }

}
