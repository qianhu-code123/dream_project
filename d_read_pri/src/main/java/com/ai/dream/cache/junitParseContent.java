package com.ai.dream.cache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class junitParseContent {

    public static void main(String[] args) throws Exception{
        String prefix = "http://www.dingdiann.com";
        String path = "/ddk8836/5307795.html";
        String url = "";
        Document doc ;
        url = prefix + path;
        doc = Jsoup.connect(url).get();
        Element jj = doc.getElementById("content");
        System.out.println(jj.toString());

    }

}
