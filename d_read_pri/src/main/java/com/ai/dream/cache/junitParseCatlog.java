package com.ai.dream.cache;

import com.ai.dream.utils.JsonTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.*;

public class junitParseCatlog {

    public static void main(String[] args) throws Exception{
        String prefix = "http://www.dingdiann.com";
        String dir = "/ddk739/";
        String url = "";
        Document doc ;
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> relMap = new HashMap<>();
        url = prefix + dir;
        doc = Jsoup.connect(url).get();
        Elements content = doc.getElementsByClass("box_con").select("div>dl").select("dd").select("a");
        System.out.println(content.size());
        for(int t=0;t<12;t++){
            content.remove(0);
        }
        System.out.println(content.size());
        Iterator it = content.iterator();
        int g = 1;
        int i = 1;
        Map<String,Object> finalMap = new HashMap<>();
        while(it.hasNext()){
            Element el = (Element) it.next();
            el.addClass(el.attr("href"));
            el.removeAttr("href");
            if(content.size()==i){
                if(g%7==0){
                    list.add(finalMap);
                    finalMap = new HashMap<>();
                    g = 1;
                    finalMap.put("catlog-"+g,el.toString());
                }else{
                    finalMap.put("catlog-"+g,el.toString());
                }
                list.add(finalMap);
            }else{
                if(g%7==0){
                    list.add(finalMap);
                    finalMap = new HashMap<>();
                    g = 1;
                    finalMap.put("catlog-"+g,el.toString());
                }else {
                    finalMap.put("catlog-"+g,el.toString());
                }
            }
            g++;
            i++;
        }
        JsonTools.object2Json(list);
    }
}
