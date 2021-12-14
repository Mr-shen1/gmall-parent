package com.atguigu.gmall.starter.cache.annotation;

import java.lang.annotation.*;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface GmallCache {


    String cacheKey() default "";



    BizLockOptions lockOptions() default @BizLockOptions;

    BloomOptions bloomOptions() default @BloomOptions;
}
