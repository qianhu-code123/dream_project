package com.ai.dream.utils;

import java.util.Arrays;

public class ClearForTest {

    /**
     * 对数组绝对值排序
     */
    public static void sortFlashArr(int[] arr){
        for(int i=0;i<arr.length-1;i++){
            for(int j=i+1;j<arr.length;j++){
                if(Math.abs(arr[i])>=Math.abs(arr[j])){
                    int temp = arr[j];
                    arr[j]=arr[i];
                    arr[i]=temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr));
    }

    public static void main(String[] args) {
       // int[] arr={3,-1,1,9,6,5,3,2,-8,-7};
        //sortFlashArr(arr);
        System.out.println();

    }

}
