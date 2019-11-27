package com.ai.dream.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qianhu
 */
public class JsonInit {

    /**
     *
     * @param code   返回编码
     * @param msg   返回信息
     * @param data  返回数据
     * @param size  返回条数
     * @return   jsonstr
     */
    public static String rebakJson(String code, String msg, Object data, long size) throws Exception{
        Map<String,Object> relMap = new HashMap<>();
        String fianlStr = "";
        try{
            relMap.put("code",code);
            relMap.put("msg",msg);
            relMap.put("data",data);
            relMap.put("count",size);
            fianlStr = JsonTools.object2Json(relMap);
        }catch (Exception e){
            throw e;
        }
        return fianlStr;
    }



}
