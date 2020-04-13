package com.ai.dream.cache;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTest implements Runnable {
    @Override
    public void run() {
        for(int i=0;i<100;i++){
            System.out.println(Thread.currentThread().getName()+"----------"+i);
        }

    }

    public static void main(String[] args) {
        // 创建一个线程池对象，控制要创建几个线程对象。
        //1. newSingleThreadExecutor：创建一个单线程的线程池，保证顺序  2.newFixedThreadPool 创建固定大小的线程池 3.newCachedThreadPool 创建一个可缓存的线程池 4.newScheduledThreadPool 创建一个大小无限的线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
//        ExecutorService pool2 = Executors.newSingleThreadExecutor();
//        ExecutorService pool3 = Executors.newCachedThreadPool();
        //ExecutorService pool4 = Executors.newScheduledThreadPool(10); //corePoolSize ------spring corePoolSize
        // 可以执行Runnable对象或者Callable对象代表的线程
        pool.submit(new ThreadPoolTest());
        pool.submit(new ThreadPoolTest());

        //结束线程池
        pool.shutdown();

    }
}
