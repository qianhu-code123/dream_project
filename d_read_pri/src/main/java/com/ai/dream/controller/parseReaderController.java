package com.ai.dream.controller;

import com.ai.dream.cache.parseQuery;
import com.ai.dream.read.services.interfaces.INetReaderSV;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonInit;
import com.ai.dream.utils.JsonTools;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class parseReaderController {

    Logger log = LoggerFactory.getLogger(parseReaderController.class);

    @RequestMapping("/read/parse/query")
    public String  parseRead(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Map<String,Object>> list = new ArrayList<>();
        String jsonStr = "";
        try{
            String keyword = request.getParameter("keyword");
            long page = Long.parseLong(request.getParameter("page"));
            long limit = Long.parseLong(request.getParameter("limit"));
            parseQuery pq = new parseQuery();
            Map<String,Object> temp = pq.getTitleInfo(keyword,page,limit);
            response.setHeader("Access-Control-Allow-Origin","*");
            jsonStr = JsonInit.rebakJson("0000","",temp.get("list"), (long) temp.get("count"));
        }catch (Exception e){
            log.error("解析错误,错误原因: "+e);
            throw  e;
        }
        return jsonStr;
    }


    @RequestMapping("/read/ddk*")
    public String parseCatlog(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        try {
            String catalog = request.getParameter("catalog");
            parseQuery pq = new parseQuery();
            List<Map<String,Object>> list = pq.getCatalog(catalog);
            response.setHeader("Access-Control-Allow-Origin","*");
            jsonStr = JsonInit.rebakJson("0000","",list, list.size());
        }catch (Exception e){
            log.error("失败原因: ",e);
            throw e;
        }
        return jsonStr;
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
