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
import redis.clients.jedis.JedisCluster;

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

    @Autowired
    private JedisCluster jedisCluster;

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
            String type = request.getParameter("type");
            long size = 0;
            int page = Integer.parseInt(request.getParameter("page"));
            int limit = Integer.parseInt(request.getParameter("limit"));
            parseQuery pq = new parseQuery();
            List<Map<String,Object>> list = new ArrayList<>();
            if("pc".equals(type)){
                list = pq.getCatalog(catalog);
                size = list.size();
            }else if("mobile".equals(type)){
                Map<String,Object> temMap = pq.getMobileCl(catalog,type,page,limit);
                list = (List<Map<String, Object>>) temMap.get("list");
                size = (long) temMap.get("count");
            }
            response.setHeader("Access-Control-Allow-Origin","*");
            jsonStr = JsonInit.rebakJson("0000","",list, size);
        }catch (Exception e){
            log.error("失败原因: ",e);
            throw e;
        }
        return jsonStr;
    }

    @RequestMapping("/read/ddk*/**.html")
    public String parseBook(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String,Object> relMap = new HashMap<>();
        String dir = request.getParameter("path");
        String userId = request.getParameter("user_id");
        String bookname = request.getParameter("bookname");
        String jsonStr = "";
        try {
            jedisCluster.set(userId,dir+"|"+bookname);
            parseQuery pq = new parseQuery();
            List<String> list = pq.getContent(dir);
            response.setHeader("Access-Control-Allow-Origin","*");
            jsonStr = JsonInit.rebakJson("0000","",list, 1);
        }catch (Exception e){
            log.error("失败原因: ",e);
            throw e;
        }
        return jsonStr;
    }


    @RequestMapping("/read/backNews")
    public String backNews(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String,Object> map = new HashMap<>();
        String userId = request.getParameter("user_id");
        String jsonStr = "";
        try {
            String path = jedisCluster.get(userId);
            if(null == path || ""==path){
                response.setHeader("Access-Control-Allow-Origin","*");
                jsonStr = JsonInit.rebakJson("-1","",null, 1);
                return jsonStr;
            }else {
                response.setHeader("Access-Control-Allow-Origin","*");
                map.put("path",path.split("|")[0]);
                map.put("bookname",path.split("|")[1]);
            }
        }catch (Exception e){
            log.error("失败原因: ",e);
            throw e;
        }
        return jsonStr;
    }



}
