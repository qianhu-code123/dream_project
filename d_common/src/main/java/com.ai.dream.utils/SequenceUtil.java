package com.ai.dream.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SequenceUtil {

    public static String getSeq(){
        SimpleDateFormat str = new SimpleDateFormat("yyyyMMddHHmmss");
        String seq = str.format(new Date());
        System.out.println(seq);
        return seq;
    }

}
