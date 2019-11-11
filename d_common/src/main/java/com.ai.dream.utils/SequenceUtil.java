package com.ai.dream.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author:qianhu 2019/11/10
 */
public class SequenceUtil {

     public static String getSeq(){
         String strDate = "";
         java.util.Date date = new Date();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH24mmss");
         strDate = formatter.format(date);
        return strDate;
    }

    public static void main(String[] args) {
        getSeq();
    }

}
