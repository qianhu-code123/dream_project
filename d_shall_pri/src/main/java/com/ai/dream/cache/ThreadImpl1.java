package com.ai.dream.cache;
//1.继承Thread来实现多线程
public class ThreadImpl1 extends Thread {

    public ThreadImpl1(){}

    public void run(){
        for(int i=0;i<100;i++){
            System.out.println(Thread.currentThread().getName()+"----------"+i);
        }
    }

    public static void main(String[] args) {
        ThreadImpl1 t1 = new ThreadImpl1();
        ThreadImpl1 t2 = new ThreadImpl1();
        ThreadImpl1 t3 = new ThreadImpl1();
        t1.start();
        t2.start();
        t3.start();
    }


}
