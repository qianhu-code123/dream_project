package com.ai.dream.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class parseTools {
    //解析电视剧
    @Cacheable(cacheNames = "parseTV",key="#mvname +''" ,unless = "#result == null")
    public  List<Map<String,String>> parseTvByName(String mvname) throws Exception{
        List<Map<String,String>> reslist = new ArrayList<>();
        Map<String,String> temps = new HashMap<>();
        String prefix = "https://so.iqiyi.com/so/q_"+mvname+"?source=input&sr=1221329183492";
        try {
            //获取dom对象
            Document doc = Jsoup.connect(prefix).get();
            //查询后有集数，取第一个，爱奇艺是(defaultlist ---不是最新  list ---最新)
           // Elements allUrl = doc.getElementsByAttribute("data-tvlist-elem").select("ul").get(1).select("ul");
            //Element allUrq =doc.getElementsByAttributeValue("class","qy-mod-link").get(0);
            //Element allUrlw= doc.getElementsByAttributeValue("data-tvlist-elem","list").get(0); //主要针对显示集数
            //有无集数判断
            Elements flag = doc.getElementsByAttributeValue("class","result-card-wrap");
            if(null == flag || flag.size() == 0 ){
                //有集数取第一栏
                Element haslevel = doc.getElementsByAttributeValue("class","qy-search-result-item vertical-pic").get(0).getElementsByAttributeValue("data-tvlist-elem","list").get(0);
                String firstNmae = doc.getElementsByAttributeValue("class","main-tit").get(0).attributes().get("title");
                int i =1;
                int k=1;
                List nodeList = haslevel.childNodes();
                Iterator it = nodeList.iterator();
                Map<String,String> temp = new HashMap<>();
                while(it.hasNext()){
                    Node ets = (Node) it.next();
                    if(!" ".equals(ets.toString()) || nodeList.size() == k){
                        if(nodeList.size() == k){
                            reslist.add(temp);
                            break;
                        }
                        if(i>6){
                            reslist.add(temp);
                            temp = new HashMap<>();
                            i=1;
                        }
                        String url = ets.childNode(1).attr("href");
                        String title = ets.childNode(1).attr("title").replace("预告","");
                        String levelStr = "<span class='"+url+"'>"+title+"</span>";
                        temp.put("level"+i,levelStr);
                        i++;
                    }
                    k++;
                }
                temps.put("title",firstNmae);
                reslist.add(temps);
            }else {
                //无集数取第一栏
                String firstName = doc.getElementsByAttributeValue("class","main-link").get(0).attributes().get("title");
                Element nolevel = doc.getElementsByAttributeValue("class","result-card-wrap").get(0).getElementsByAttributeValue("class","qy-mod-link").get(0);
                String newurl ="http:"+ nolevel.attributes().get("href");
                //获取dom对象
                Document newdoc = Jsoup.connect(newurl).get();
                Elements ets = newdoc.getElementsByAttributeValue("data-albumlist-play","list").select("li").select("a");
                int p=1;
                int k=0;
                int j=1;
                int i=1;
                Iterator it = ets.iterator();
                Map<String,String> temp = new HashMap<>();
                while (it.hasNext()){
                    Element t = (Element) it.next();
                    int x =3*j-2;
                    if(k == x ||ets.size() == p){
                        if(ets.size() == p){
                            if(i>6){
                                reslist.add(temp);
                                temp = new HashMap<>();
                                i=1;
                                String newlevel = t.childNode(0).toString();
                                String nolevelUrl =t.select("a").get(0).attributes().get("href").replace("//","");
                                String levelStr = "<span class='"+nolevelUrl+"'>"+newlevel+"</span>";
                                temp.put("level"+i,levelStr);
                            }else {
                                String newlevel = t.childNode(0).toString();
                                String nolevelUrl =t.select("a").get(0).attributes().get("href").replace("//","");
                                String levelStr = "<span class='"+nolevelUrl+"'>"+newlevel+"</span>";
                                temp.put("level"+i,levelStr);
                            }
                            reslist.add(temp);
                        }else {
                            if(i>6){
                                reslist.add(temp);
                                temp = new HashMap<>();
                                i=1;
                            }
                            String newlevel = t.childNode(0).toString();
                            String nolevelUrl =t.select("a").get(0).attributes().get("href").replace("//","");
                            String levelStr = "<span class='"+nolevelUrl+"'>"+newlevel+"</span>";
                            temp.put("level"+i,levelStr);
                            i++;
                            j++;
                        }
                    }
                    k++;
                    p++;
                }
                temps.put("title",firstName);
                reslist.add(temps);
            }
        }catch (Exception e){
            throw e;
        }
       return reslist;
    }

    //解析电影 data-playsrc-elem="firstlink"
    @Cacheable(cacheNames = "parseMV",key="#mvname +''" ,unless = "#result == null")
    public  List<Map<String,String>>  parseMvByName(String mvname) throws Exception{
        String url = "";
        String title = "";
        Map<String,String> mvMap = new HashMap<>();
        List<Map<String,String>> relist = new ArrayList<>();
        String prefix = "https://so.iqiyi.com/so/q_"+mvname+"?source=input&sr=1221329183492";
        try {
            //获取dom对象
            Document doc = Jsoup.connect(prefix).get();
            Elements ste = doc.getElementsByAttributeValue("class","main-tit").get(0).select("span");
            //查询后有集数，取第一个，爱奇艺是(defaultlist ---不是最新  list ---最新)
            Element allUrl = (Element) doc.getElementsByAttributeValue("class","qy-mod-link-wrap").get(0).childNode(1);
            url = allUrl.attributes().get("href");
            title = allUrl.select("img").get(0).attributes().get("title");
            String levelStr = "<span class='"+url+"'>"+title+"</span>";
            mvMap.put("level"+1,levelStr);
            relist.add(mvMap);
        }catch (Exception e){
            throw e;
        }
        return relist;
    }





}
