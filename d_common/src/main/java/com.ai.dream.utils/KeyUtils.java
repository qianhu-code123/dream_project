package com.ai.dream.utils;

import java.util.Random;

public class KeyUtils {

    /**
     * 产生独一无二的key
     */
    public static synchronized String genUniqueKey(){
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        String key = System.currentTimeMillis() + String.valueOf(number);
        return MD5Utils.getMd5(key);
    }


    public static void main(String[] args) {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        String key = System.currentTimeMillis() + String.valueOf(number);
        System.out.println(key.substring(8,16));
    }
}