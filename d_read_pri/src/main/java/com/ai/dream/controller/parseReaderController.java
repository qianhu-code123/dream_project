package com.ai.dream.controller;

import com.ai.dream.read.services.interfaces.INetReaderSV;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonTools;
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
import java.util.HashMap;
import java.util.Map;

@RestController
public class parseReaderController {

    Logger log = LoggerFactory.getLogger(parseReaderController.class);

    @Autowired
    private INetReaderSV isv;

    @RequestMapping("/read/parse/query")
    public String parseRead(HttpServletRequest request) throws Exception{
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
                    Elements elts = doc.getElementsByAttribute("novelslist2");
                    log.info(elts.html());
                }

            }
        }catch (Exception e){
            log.error("解析错误,错误原因: "+e);
            throw  e;
        }

        return null;
    }





}
