package com.ai.dream.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class shallGamesCotroller  {
    Logger log = LoggerFactory.getLogger(shallGamesCotroller.class);

    @RequestMapping("/shall/games/save")
    public Map<String,Object> save(HttpServletRequest request) throws  Exception{
        log.info("请求参数："+request.getParameter("params"));
        Map<String ,Object> relMap = new HashMap<>();
        String fialStr = "";
        try {



        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }

        return null;
    }

}
