package com.ai.dream.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTools {
    public static ObjectMapper mapper = new ObjectMapper();

    /***
     * 将对象序列化为JSON文本  MAP,LIST,OBJECT都适用
     *
     * @param object
     * @return
     */
    public static String object2Json(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    /**
     * 将json字符串转化成对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T json2Object(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }


    /**
     * JSON字符串转化成Map对象具体需要转
     *  具体转换类型，参考Map<String,User> result = mapper.readValue(src, new TypeReference<Map<String,User>>()
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> json2Map(String jsonStr) throws IOException {
        return mapper.readValue(jsonStr, Map.class);
    }


    /**
     * JSON字符串转化成对象List
     *
     * @param jsonStr
     * @return
     */
    public static <T> List<T> json2List(String jsonStr) throws IOException {
        return mapper.readValue(jsonStr, List.class);

    }


    public static Object getValueInMap(Map<String, Object> map, String keyStr) {
        Object obj = map.get(keyStr);
        if (obj == null) {
            obj = map.get(keyStr.toUpperCase());
        }
        return obj;
    }

    /**
     * 将对象转化成属性全部转化为大写的Map对象
     *
     * @param obj
     * @return
     */
    public static <T> Map<String, Object> objectToUpperCaseMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (obj != null) {
                Class<?> clazz = obj.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                    Object entity = obj.getClass().getMethod(methodName, new Class[0]).invoke(obj, new Object[0]);
                    map.put(name.toUpperCase(), entity == null ? "" : entity);
                }
            }
        } catch (Exception e) {
        }
        return map;
    }

}