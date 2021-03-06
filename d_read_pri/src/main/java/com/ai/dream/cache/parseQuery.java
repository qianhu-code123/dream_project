package com.ai.dream.cache;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class parseQuery {

    @Cacheable(cacheNames = "dealParse",key = "#keyword + ''", unless = "#result == null")
    public Map<String,Object> getTitleInfo(String keyword, long page ,long limit) throws Exception{
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> finalMap = new HashMap<>();
        String prefix = "http://www.dingdiann.com/searchbook.php?";
        try {
            long k = page * limit;
            long j = k - 10;
            String url = prefix + "keyword="+keyword;
            //获取dom对象
            Document doc = Jsoup.connect(url).get();
            //main 元素
            Element elt = doc.getElementById("main");
            //li元素   列表
            Elements elts = elt.select("ul").select("li");
            Iterator it = elts.iterator();
            int i =0;
            while(it.hasNext()){
                if(i>0){
                    if(i<=k && i>j) {
                        Map<String, Object> relMap = new HashMap<>();
                        Element el = (Element) it.next();
                        relMap.put("bookType", el.child(0).childNode(0).toString());
                        relMap.put("bookName", el.child(1).childNode(0).toString());
                        relMap.put("newTitle", el.child(2).childNode(0).toString());
                        relMap.put("author", el.child(3).childNode(0).toString());
                        relMap.put("clickTime", el.child(4).childNode(0).toString());
                        relMap.put("upDate", el.child(5).childNode(0).toString());
                        relMap.put("state", el.child(6).childNode(0).toString());
                        list.add(relMap);
                    }else{
                        it.next();
                    }
                }else{
                    it.next();
                }
                i++;
            }
            finalMap.put("list",list);
            finalMap.put("count",Long.parseLong((elts.size()-1)+"") );
        }catch (Exception e){
            throw e;
        }
        return finalMap;
    }

    @Cacheable(cacheNames = "dealCatalog",key = "#catalog + ''", unless = "#result == null")
    public List<Map<String,Object>> getCatalog(String catalog) throws Exception{
        String url = "http://www.dingdiann.com/"+catalog+"/";
        Document doc ;
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            doc = Jsoup.connect(url).get();
            Elements content = doc.getElementsByClass("box_con").select("div>dl").select("dd").select("a");
            for(int t=0;t<12;t++){
                content.remove(0);
            }
            Iterator it = content.iterator();
            int g = 1;
            int i = 1;
            Map<String,Object> finalMap = new HashMap<>();
            while(it.hasNext()){
                Element el = (Element) it.next();
                //将超链接去掉，属性值放到class中
                el.addClass(el.attr("href"));
                el.removeAttr("href");
                if(content.size()==i){
                    if(g%7==0){
                        list.add(finalMap);
                        finalMap = new HashMap<>();
                        g = 1;
                        finalMap.put("catlog"+g,el.toString());
                    }else{
                        finalMap.put("catlog"+g,el.toString());
                    }
                    list.add(finalMap);
                }else{
                    if(g%7==0){
                        list.add(finalMap);
                        finalMap = new HashMap<>();
                        g = 1;
                        finalMap.put("catlog"+g,el.toString());
                    }else {
                        finalMap.put("catlog"+g,el.toString());
                    }
                }
                g++;
                i++;
            }

        }catch (Exception e){
            throw e;
        }
        return list;
    }

    @Cacheable(cacheNames = "dealMobileCl",key = "#catalog + #type+ ''", unless = "#result == null")
    public Map<String,Object> getMobileCl(String catalog, String type, int page, int limit) throws Exception{
        Map<String,Object> finalMap = new HashMap<>();
        String url = "http://www.dingdiann.com/"+catalog+"/";
        Document doc ;
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            int k = page * limit;
            int j = k - limit;
            doc = Jsoup.connect(url).get();
            Elements content = doc.getElementsByClass("box_con").select("div>dl").select("dd").select("a");
            for(int t=0;t<12;t++){
                content.remove(0);
            }
            Iterator it = content.iterator();
            int i = 1;
            while(it.hasNext()){
                if(i<=k && i>j) {
                    Map<String, Object> relMap = new HashMap<>();
                    Element el = (Element) it.next();
                    //将超链接去掉，属性值放到class中
                    el.addClass(el.attr("href"));
                    el.removeAttr("href");
                    relMap.put("catlog",el.toString());
                    list.add(relMap);
                }else{
                    it.next();
                }
                i++;
            }
            finalMap.put("list",list);
            finalMap.put("count",Long.parseLong(content.size()+"") );
        }catch (Exception e){
            throw e;
        }
        return finalMap;
    }




    @Cacheable(cacheNames = "getContent",key = "#path + ''", unless = "#result == null")
    public  List<String> getContent(String path) throws Exception{
        String prefix = "http://www.dingdiann.com";
        List<String> newe = new ArrayList<>();
        String url = "";
        Document doc ;
        try {
            url = prefix + path;
            doc = Jsoup.connect(url).get();
            Element jj = doc.getElementById("content");
            Elements tl = doc.getElementsByClass("bookName").select("h1");
            String name = tl.get(0).toString();
            String str = jj.toString();
            String[] s =str.split("\n");
            List<String> list=new ArrayList<>();
            Collections.addAll(list,s);
            list.remove(0);
            list.remove(list.size()-1);
            list.remove(list.size()-1);
            Iterator it = list.iterator();
            while(it.hasNext()){
                newe.add((String) it.next());
            }
            newe.add(name);
        }catch (Exception e){
            throw e;
        }

        return newe;
    }


}
