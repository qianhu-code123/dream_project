package com.ai.dream.cache;
//1.实现Runnable接口来实现多线程
public class ThreadImpl2 implements Runnable {

    public void run(){
        for(int i=0;i<100;i++){
            System.out.println(Thread.currentThread().getName()+"----------"+i);
        }
    }

    public static void main(String[] args) {
        ThreadImpl2 t = new ThreadImpl2();
        Thread m1 = new Thread(t,"线程1");
        Thread m2 = new Thread(t,"线程2");
        Thread m3 = new Thread(t,"线程3");
        m1.start();
        m2.start();
        m3.start();
    }


}
