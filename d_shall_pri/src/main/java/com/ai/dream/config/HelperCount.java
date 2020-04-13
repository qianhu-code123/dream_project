package com.ai.dream.config;

public class HelperCount {

    public static String getString(String level) throws Exception{
        try {
            if("L".equals(level)){
                return "学习资料";
            }else if("VL".equals(level)){
                return "<span style='color:red'>精品课程<span>";
            }else if("G".equals(level)){
                return "游戏资源";
            }else if("VG".equals(level)){
                return "<span style='color:red'>精品游戏<span>";
            }else if("M".equals(level)){
                return "电影资源";
            }else if("VM".equals(level)){
                return "<span style='color:red'>精品电影<span>";
            }
        }catch (Exception e){
            throw e;
        }

        return null;
    }

}
