package com.atguigu.gmall.starter.cache.annotation;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
public @interface BloomOptions {

    String bloomName() default "";

    String bloomExp() default "";
}
