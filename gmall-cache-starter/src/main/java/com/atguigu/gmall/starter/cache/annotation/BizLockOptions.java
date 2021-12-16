package com.atguigu.gmall.starter.cache.annotation;

import java.lang.annotation.*;

/**
 * desc: 用于设置锁的等待锁的时间后, 获取锁后释放的时间
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

    int lockReleaseTime() default 6; // 时间一般为业务时间的两倍



}
