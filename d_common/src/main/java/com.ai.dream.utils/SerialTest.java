package com.ai.dream.utils;

import java.io.*;
import java.lang.reflect.Field;

public class SerialTest {


    public static void main(String[] args) throws Exception{
        /*Class cls = Class.forName("com.ai.dream.utils.netCollection");
        netCollection net = (netCollection) cls.newInstance();
        Field field = cls.getDeclaredField("id");
        field.setAccessible(true);
        field.set(net,100000L);
        System.out.println("开始序列化:"+net.toString());
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:\\github\\dream_project\\d_common\\src\\main\\java\\test.txt"));
        oos.writeObject(net);
        oos.close();

        System.out.println("----------------------------");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:\\github\\dream_project\\d_common\\src\\main\\java\\test.txt"));
        netCollection ttt = (netCollection) ois.readObject();
        System.out.println(ttt.toString());
        ois.close();*/

        /*
        netCollection st = new netCollection();
        Field filed = st.getClass().getDeclaredField("id");
        filed.setAccessible(true);
        filed.set(st,10000L);
        System.out.pri、ntln(st.getClass().getMethod("toString").invoke(st));
        System.out.println(st.toString());
        */
        netCollection net = new netCollection();
        net.setId(10000L);
        net.setHot("hotnew");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:/github/dream_project/d_common/src/main/java/test.txt"));
        oos.writeObject(net);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:/github/dream_project/d_common/src/main/java/test.txt"));
        netCollection newnet = (netCollection) ois.readObject();
        System.out.println(newnet.toString());
    }

}
