package com.ai.dream.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isValuable(String str){
        if(null != str && !"".equals(str)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isValuable(Map paramMap){
        return paramMap==null?false:true;
    }
    /*多个字符串判空串*/
    public static boolean isValuable(String ...strs) {
        if (strs.length >= 1) {
            Boolean flag = true;
            for (int i = 0; i < strs.length; i++) {
                if (null == strs[i] || "".equals(strs[i])) {
                    flag = false;
                    break;
                }
            }
            return flag;
        }else{
            return false;
        }
    }
    /*多重集合判空串*/
    public static boolean isValuable(Map ...paramMaps) {
        if (paramMaps.length >= 1) {
            Boolean flag = true;
            for (int i = 0; i < paramMaps.length; i++) {
                if (null == paramMaps[i]) {
                    flag = false;
                    break;
                }
            }
            return flag;
        }else{
            return false;
        }
    }

    /*判断集合是否为空，并且内面的元素是否是空串*/
    public static boolean isContentVal(Map<String,Object> paramMap){
        if(paramMap != null){
            Boolean flag = true;
            for( String key:paramMap.keySet()){
                    if(null == paramMap.get(key) || "".equals(paramMap.get(key))){
                        flag = false;
                        break;
                    }
            }
            return flag;
        }else {
            return false;
        }
    }


    //在一个字符串中，对<***/> ----> <***></***>
    public static String xmlTrans(String longStr){
            if(longStr.contains("/>")){
                Pattern p = Pattern.compile("<(.*?)/>");
                Matcher m = p.matcher(longStr);
                List<String> tempList = new ArrayList<String>();
                while(m.find()) {
                    tempList.add(m.group());
                }
                Iterator it = tempList.iterator();
                while(it.hasNext()){
                  String tempStr = (String) it.next();
                 //</ProdInfo><ProdInfo><UserOrderInfo/>
                 String[] midArr = tempStr.split(">");
                 String delStr = midArr[midArr.length-1]+">";
                 String midStr1 = delStr.replace("<","");
                 String midStr = midStr1.replace("/>","");
                 String finalStr = "<"+midStr+">"+"</"+midStr+">";
                 longStr = longStr.replace(delStr,finalStr);
                }
                return longStr;
            }else{
                return longStr;
            }
    }


    public static void main(String[] args){

       /* Map<String,Object> tmpMap = new HashMap();
        tmpMap.put("userId","22");
        tmpMap.put("username","qh2333");
        tmpMap.put("requireAuthor","admin");
        tmpMap.put("cardLevel","B");
        Boolean remap = StringUtils.isContentVal(tmpMap);
        System.out.println(remap);*/
        System.out.println(String.format("dda 312312312"));
        //String delStr = "</AttrStatus><EffTime>20130702000000</EffTime><ExpTime>20310101000000</ExpTime></ProdAttrInfo><ProdAttrInfo><InsAttrId>30000237562278</InsAttrId><AttrId>260000009995</AttrId><AttrValue>2013-07-02</AttrValue><PreAttrValue>2013-07-02</PreAttrValue><AttrStatus>1</AttrStatus><EffTime>20130702000000</EffTime><ExpTime>20310101000000</ExpTime></ProdAttrInfo></ProdExtInfo></ProdInfo><ProdInfo><ProdInstId>30002200196650</ProdInstId><ServiceId>270000091175</ServiceId><ProdId>310091175001</ProdId><UserOrderInfo/><ProdStatus>1</ProdStatus><ProdType>SRVC_SINGLE</ProdType><EffTime>20141027175124</EffTime><ExpTime>20991231235959</ExpTime><ProdExtInfo/></ProdInfo><ProdInfo><UserOrderInfo/><temp1/><temp2/>";
        //xmlTrans(delStr);

    }

}
