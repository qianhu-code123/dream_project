package com.ai.dream.controller;

import com.ai.dream.tools.parseTools;
import com.ai.dream.utils.JsonInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class aqyiParseMvController {

    Logger log = LoggerFactory.getLogger(aqyiParseMvController.class);

    @RequestMapping("/online/parseMV")
    public String parseMV(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String jsonStr = "";
        List<Map<String,String>> relist = new ArrayList<>();
        Map<String,String> relMap = new HashMap<>();
        try{
            String mvname = request.getParameter("mvname");
            String type = request.getParameter("type");
            if(null == mvname || "".equals(mvname)){
                mvname = "海贼王";
            }
            if(null == type || "".equals(type)){
                type = "soap";
            }
            parseTools p = new parseTools();
            if("movie".equals(type)){ relist = p.parseMvByName(mvname);}
            if("soap".equals(type)){ relist = p.parseTvByName(mvname);}
            jsonStr = JsonInit.rebakJson("0000","",relist,relist.size());
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因："+e.getMessage());
            jsonStr = JsonInit.rebakJson("-1",e.getMessage(),"",0);
            response.setHeader("Access-Control-Allow-Origin","*");
        }
        return jsonStr;
    }

}
