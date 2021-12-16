package com.atguigu.gmall.starter.thread;

import com.atguigu.gmall.starter.thread.properties.ThreadPoolProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * desc: 自定义线程池
 *
 * @author: skf
 * @date: 2021/12/15
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolAutoConfiguration {


    /**
     * int corePoolSize, 核心线程数
     * int maximumPoolSize, 最大线程数
     * long keepAliveTime, 当没有任务时, 非核心线程最长存活时间
     * TimeUnit unit, 单位
     * BlockingQueue<Runnable> workQueue, 等待队列
     * ThreadFactory threadFactory, 线程工厂
     * RejectedExecutionHandler handler 拒绝策略
     *
     * @return
     */
    //"itemThreadPool"
    @Bean
    public ThreadPoolExecutor getItemThreadPool(ThreadPoolProperties properties) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),
                new ItemThreadFactory(properties.getName()),
                new ThreadPoolExecutor.CallerRunsPolicy());

        System.out.println("getItemThreadPool创建完毕");
        return threadPoolExecutor;
    }

    //
    //@Bean("productThreadPool")
    //public ThreadPoolExecutor getProductThreadPool(ThreadPoolProperties properties) {
    //
    //    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(properties.getCorePoolSize(),
    //            properties.getMaximumPoolSize(),
    //            properties.getKeepAliveTime(),
    //            TimeUnit.SECONDS,
    //            new LinkedBlockingDeque<>(10),
    //            new ItemThreadFactory(properties.getName()),
    //            new ThreadPoolExecutor.CallerRunsPolicy());
    //
    //    return threadPoolExecutor;
    //}
}
