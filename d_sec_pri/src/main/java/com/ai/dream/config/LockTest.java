package com.ai.dream.config;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LockTest extends Thread{

    public LockTest(){};

    public void run(){
        new LockTest().setA("A--->B");
    }

    synchronized void setA(String string){
        System.out.println(Thread.currentThread().getName()+string);
        setB(string);
    }

    synchronized void setB(String string){
        System.out.println(Thread.currentThread().getName()+string);
    }


    public static void main(String[] args) {
       /* ExecutorService service = Executors.newFixedThreadPool(3);
        service.submit(new Runnable() {
            @Override
            public void run() {
                new LockTest().setA("A-->B");
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                new LockTest().setA("A-->B");
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                new LockTest().setA("A-->B");
            }
        });
        service.shutdown();*/

        /*LockTest t1 = new LockTest();
        LockTest t2 = new LockTest();
        LockTest t3 = new LockTest();
        LockTest t4 = new LockTest();
        t1.start();
        t2.start();
        t3.start();
        t4.start();*/


    }


}
