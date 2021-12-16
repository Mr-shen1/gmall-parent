package com.atguigu.gmall.starter.cache.annotation;

/**
 * desc: 定义布隆的名字和需要查的值
 *
 * @author: skf
 * @date: 2021/12/14
 */
public @interface BloomOptions {

    String bloomName() default "";

    String bloomExp() default "";
}
