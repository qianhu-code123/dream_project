package com.ai.dream.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

//1.继承Thread来实现多线程
public class ThreadImpl3 implements Callable<String> {

    @Override
    public String call() throws Exception {
        for(int i=0;i<100;i++){
            System.out.println(Thread.currentThread().getName()+"----------"+i);
        }
        return "final ok!";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> callable = new ThreadImpl3();
        FutureTask<String> futureTask = new FutureTask(callable);
        Thread t1 = new Thread(futureTask);
        Thread t2 = new Thread(futureTask);
        Thread t3 = new Thread(futureTask);
        t1.start();
        t2.start();
        t3.start();
        System.out.println(futureTask.get()+"*****************************");//获取线程返回值
        //t1.start();
        //t2.start();
        //t3.start();
    }



}
