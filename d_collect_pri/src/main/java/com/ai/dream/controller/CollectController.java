package com.ai.dream.controller;

import com.ai.dream.collect.netCollection;
import com.ai.dream.service.interfaces.ICollectionSV;
import com.ai.dream.utils.DateUtils;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonTools;
import com.ai.dream.utils.SequenceUtil;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CollectController {

    Logger log = LoggerFactory.getLogger(CollectController.class);

    @Autowired
    private ICollectionSV isv;

    @RequestMapping("/collect/queryAll")
    public List<Map<String,Object>> queryAll() throws Exception{
        try{
            return isv.queryAll();
        }catch (Exception e){
            log.error("查询异常：",e);
            throw e;
        }

    }

    @RequestMapping("/collect/queryById")
    public Map<String,Object> queryById(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        String jsonStr = "";
        Map<String,Object> relMap = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        try{
            if(!EmptyUtil.isEmpty(request.getParameter("params"))){
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                if(!EmptyUtil.isEmpty(relMap.get("id"))){
                    map = isv.queryById(relMap);
                }
            }
        }catch (Exception e){
            log.error("查询异常：",e);
            throw e;
        }
        return map;
    }

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping("/collect/addnew")
    public boolean addnew(HttpServletRequest request)throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        //id, title, morder, hot, create_date,state
        boolean flag = false;
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        try{
            if(!EmptyUtil.isEmpty(request.getParameter("params"))){
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                if(!EmptyUtil.isEmpty(relMap.get("title")) && !EmptyUtil.isEmpty(relMap.get("create_date"))){
                    java.util.Date strDATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(relMap.get("create_date").toString());
                    relMap.put("id", SequenceUtil.getSeq());
                    flag = isv.addnew(relMap);
                }
            }
        }catch (Exception e){
            log.error("添加失败，失败原因：",e);
            throw e;
        }
        return flag;
    }

    @RequestMapping("/collect/delById")
    public boolean delById(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        String jsonStr = "";
        boolean flag = false;
        Map<String,Object> relMap = new HashMap<>();
        try {
            if(!EmptyUtil.isEmpty(request.getParameter("params"))){
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                if(!EmptyUtil.isEmpty(relMap.get("id"))){
                    flag = isv.delById(relMap);
                }
            }
        }catch (Exception e){
            log.error("删除失败：",e);
            throw e;
        }

        return flag;
    }

    @RequestMapping("/collect/updnew")
    public boolean updnew(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        boolean flag = false;
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        try{
            if(!EmptyUtil.isEmpty(request.getParameter("params"))){
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                if(!EmptyUtil.isEmpty(relMap.get("id")) && !EmptyUtil.isEmpty(relMap.get("title")) && !EmptyUtil.isEmpty(relMap.get("create_date"))){
                    Map<String,Object> tMap = isv.queryById(relMap);
                    if(!EmptyUtil.isEmpty(tMap)){
                        flag = isv.updnew(relMap);
                    }else {
                        throw new Exception("更新失败，当前记录不存在");
                    }

                }
            }
        }catch (Exception e){
            log.error("更新失败，失败原因：",e);
            throw e;
        }
        return flag;
    }



}
