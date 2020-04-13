package com.ai.dream.controller;

import com.ai.dream.utils.JsonInit;
import com.ai.dream.worknote.services.impl.WorknoteSVImpl;
import com.ai.dream.worknote.services.interfaces.IWorknoteSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class worknoteController {

    Logger log = LoggerFactory.getLogger(worknoteController.class);

    @Autowired
    IWorknoteSV sv;

    @RequestMapping("/worknote/save")
    public String saveNote(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String ,Object> relMap = new HashMap<>();
        String jsonStr = "";
        try {
            relMap.put("note_title",request.getParameter("title"));
            relMap.put("note_date",request.getParameter("date"));
            relMap.put("work_content",request.getParameter("content"));
            relMap.put("state","U");
            sv.saveNote(relMap);
            jsonStr = JsonInit.rebakJson("0000","success",relMap,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            jsonStr = JsonInit.rebakJson("-1","fail",null,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }
        return jsonStr;
    }

    @RequestMapping("/worknote/initAllNote")
    public String initAllNote(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String ,Object> relMap = new HashMap<>();
        String jsonStr = "";
        try {
            relMap.put("type",request.getParameter("type"));
            List<Map<String,Object>> reslist = sv.initAllNote(relMap);
            jsonStr = JsonInit.rebakJson("0000","success",reslist,reslist.size());
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因： "+e.getMessage());
            jsonStr = JsonInit.rebakJson("-1","fail",null,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }
        return jsonStr;
    }

    @RequestMapping("/worknote/queryByTitle")
    public String queryByTitle(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String ,Object> relMap = new HashMap<>();
        String jsonStr = "";
        try {
            relMap.put("note_title",request.getParameter("note_title"));
            List<Map<String,Object>> reslist = sv.queryByTitle(relMap);
            jsonStr = JsonInit.rebakJson("0000","success",reslist,reslist.size());
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因： "+e.getMessage());
            jsonStr = JsonInit.rebakJson("-1","fail",null,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }
        return jsonStr;
    }
}

