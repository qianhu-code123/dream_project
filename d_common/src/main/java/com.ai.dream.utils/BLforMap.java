package com.ai.dream.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

public class BLforMap {

    private String test;

    private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setTest(String test){
        this.test=test;
    }

    public String getTest() {
        return test;
    }

    public void keySetMap(HashMap<String,Object> map){
        for(String key:map.keySet()){
            System.out.println("key:"+key+"-----value:"+map.get(key));
        }
    }

    public void itratorMap(HashMap<String,Object> map){
        Iterator it = map.keySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Object> entry = (Map.Entry<String, Object>) it.next();
            String key = entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("key:"+key+"---value:"+value);
        }
    }

    public void forMapEntry(HashMap<String,String> map){
        for(Map.Entry<String,String> entry:map.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("key:"+key+"---value:"+value);
        }
    }

    public static void main(String[] args) throws Exception {
        //反射创建对象过程
        Class mapobj = Class.forName("com.ai.dream.utils.BLforMap");
        BLforMap map = (BLforMap) mapobj.newInstance();
        //反射获取私有成员变量，并set值，不需要对象有set方法
        Field field = mapobj.getDeclaredField("test");
        field.setAccessible(true);
        field.set(map,"testaa");
        System.out.println(map.getTest());
        //获取所有属性值 ,getDeclaredFields()------filed[i].get(obj)
        map.setAge("14");
        map.setTest("testbb");
        Field[] declaredFields = mapobj.getDeclaredFields();
        for(int i=0;i<declaredFields.length;i++){
            declaredFields[i].setAccessible(true);
            System.out.println(declaredFields[i].get(map));
        }
        //获取方法 Class对象.getMethod("methodName",参数类型.class  eg String.class  HashMap.class)  method对象.invoke(对象,参数值)
        HashMap<String,String> newMap = new HashMap<String,String>();
        newMap.put("test","123");
        Method method = mapobj.getMethod("keySetMap", HashMap.class);
        method.invoke(map,newMap);

       //获取所有方法
        Method[] methods = mapobj.getMethods();
        for(Method methoda : methods){
            Class<?>[] parameterTypes = methoda.getParameterTypes();
            for(int i=0;i<parameterTypes.length;i++){
                System.out.println(parameterTypes[i].getName()+"");
            }
        }
        

    }


}
