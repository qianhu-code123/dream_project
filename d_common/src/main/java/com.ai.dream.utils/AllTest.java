package com.ai.dream.utils;

public class AllTest {

    //思路 往右移1位 比如6  0110   6>>1 --->  0011 继续 3>>1 0001  count+1 ; 1>>1 count+1
    public static void testA(){
        int c=0;
        int n = 6;
        while (n >0)
        {
            if((n &1) ==1) // 当前位是1
                ++c ; // 计数器加1
            n >>=1 ; // 移位
        }
        System.out.println(c);
    }

    public static void testB(){
        String key = "test";
        int h;
        int x = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        System.out.println(x);
    }

    public static void main(String[] args) {
        //System.out.println(3556498^(3556498 >>16)); //^是按位异或运算，把前面的转为二进制，两种结果相同的位为0否则为1
        System.out.println(3 &1);

        testA();

    }

}
