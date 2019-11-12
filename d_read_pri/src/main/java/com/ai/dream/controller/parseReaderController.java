package com.ai.dream.controller;

import com.ai.dream.read.services.interfaces.INetReaderSV;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonTools;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class parseReaderController {

    Logger log = LoggerFactory.getLogger(parseReaderController.class);

    @Autowired
    private INetReaderSV isv;

    @RequestMapping("/read/parse/query")
    public Map<String,Object> parseRead(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        String prefix = "http://www.dingdiann.com/searchbook.php?";
        String url = "";
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        Document doc;
        try{
            if(!EmptyUtil.isEmpty(request.getParameter("params"))){
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                if(!EmptyUtil.isEmpty(relMap.get("keyword"))){
                    url = prefix + "keyword="+relMap.get("keyword");
                    doc = Jsoup.connect(url).get();
                    Element elt = doc.getElementById("main");
                    Elements elts = elt.select("ul>li");
                    relMap.put("data",elts.toString());
                    /*jsonStr = JsonTools.object2Json(relMap);*/

                    return relMap;
                }

            }
        }catch (Exception e){
            log.error("解析错误,错误原因: "+e);
            throw  e;
        }

        return null;
    }


    @RequestMapping("/read/ddk*")
    public Map<String,Object> parseCatlog(HttpServletRequest request) throws Exception{

        log.info("请求path： "+request.getServletPath());
        Map<String,Object> relMap = new HashMap<>();
        String dir = request.getServletPath();
        String prefix = "http://www.dingdiann.com";
        String url = "";
        Document doc ;
        StringBuffer stringBuffer = new StringBuffer("");
        try {
            url = prefix + dir;
            doc = Jsoup.connect(url).get();
            //简介部分
            Elements jj = doc.getElementsByClass("maininfo");
            log.info("简介："+jj.toString());
            relMap.put("jj",jj.toString());
            Elements content = doc.getElementsByClass("box_con").select("div>dl").select("dd");
            for(int i=1;i<content.size();i++){
                stringBuffer = stringBuffer.append(content.get(i));
            }
            log.info("内容："+stringBuffer.toString());
            relMap.put("catlog",stringBuffer.toString());

            return relMap;

        }catch (Exception e){
            log.error("失败原因: ",e);
            throw e;
        }
    }

    @RequestMapping("/read/ddk*/**.html")
    public Map<String, Object> parseBook(HttpServletRequest request) throws Exception{

        log.info("请求path： "+request.getServletPath());
        Map<String,Object> relMap = new HashMap<>();
        String dir = request.getServletPath();
        String prefix = "http://www.dingdiann.com";
        String url = "";
        Document doc ;
        StringBuffer stringBuffer = new StringBuffer("");
        try {
            url = prefix + dir;
            doc = Jsoup.connect(url).get();
            //简介部分
            Element jj = doc.getElementById("content");
            log.info("内容："+jj.toString());
            relMap.put("content",jj.toString());
            return relMap;

        }catch (Exception e){
            log.error("失败原因: ",e);
            throw e;
        }
    }






}
