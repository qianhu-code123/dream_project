package com.ai.dream.cache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.*;

public class junitParseHTML {

    public static void main(String[] args) throws Exception{
        List<Map<String,Object>> list = new ArrayList<>();
        Document doc;
        String prefix = "http://www.dingdiann.com/searchbook.php?";
        String url = prefix + "keyword=大主宰";
        long limit = 10;
        long page =3;

        // 1   (0,10]    2  (10,20]  3 (20,30]
        long k = page * limit;
        long j = k - 10;

        //获取dom对象
        doc = Jsoup.connect(url).get();
        //main 元素
        Element elt = doc.getElementById("main");
        //li元素   列表
        Elements elts = elt.select("ul").select("li");
        Iterator it = elts.iterator();
        int i =0;

        while(it.hasNext()){
            if(i>0){
                if(i<=k && i>j){
                    Map<String,Object> relMap = new HashMap<>();
                    Element el = (Element) it.next();
                    Node bookType = el.child(0).childNode(0);
                    Node bookName = el.child(1).childNode(0);
                    Node newTitle = el.child(2).childNode(0);
                    Node author = el.child(3).childNode(0);
                    Node clickTime = el.child(4).childNode(0);
                    Node upDate = el.child(5).childNode(0);
                    Node state = el.child(6).childNode(0);
                    list.add(relMap);
                    System.out.println("bookType="+bookType.toString()+"---"+"bookName="+bookName.toString()+"---"+"newTitle="+newTitle.toString()+"---"+"author="+author.toString()+"---"+"clickTime="+clickTime.toString()+"---"+"upDate="+upDate.toString()+"---"+"state="+state.toString());
                }else {
                    it.next();
                }
            }else{
                it.next();
            }
            i++;

        }

    }








}
