package com.atguigu.gmall.starter.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * desc: 自定义的item线程工厂
 *
 * @author: skf
 * @date: 2021/12/15
 */
public class ItemThreadFactory implements ThreadFactory {
    private String name;

    public ItemThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {

        AtomicInteger count = new AtomicInteger();

        Thread thread = new Thread(r);
        thread.setName(name + count.getAndIncrement());
        return thread;
    }
}
