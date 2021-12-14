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
public @interface BizLockOptions {

    int lockTime() default 3;

    int lockReleaseTime() default 6; // 时间为业务时间的两倍



}
