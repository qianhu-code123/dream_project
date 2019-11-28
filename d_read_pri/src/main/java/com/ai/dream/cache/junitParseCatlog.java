package com.ai.dream.cache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class junitParseCatlog {

    public static void main(String[] args) throws Exception{
        String prefix = "http://www.dingdiann.com";
        String dir = "/ddk739/";
        String url = "";
        Document doc ;
        Map<String,Object> relMap = new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer("");
        url = prefix + dir;
        doc = Jsoup.connect(url).get();
        Elements content = doc.getElementsByClass("box_con").select("div>dl").select("dd").select("a");
        Iterator it = content.iterator();
        int i=1;
        while(it.hasNext()){
            if(i>12){
                Element el = (Element) it.next();
                System.out.println(el.toString());
            }else {
                it.next();
            }
            i++;
        }
    }

}
