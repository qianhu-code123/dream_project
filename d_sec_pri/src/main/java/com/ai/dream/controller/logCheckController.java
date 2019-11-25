package com.ai.dream.controller;

import com.ai.dream.sec.service.interfaces.IRegisterSV;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonTools;
import com.ai.dream.utils.KeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class logCheckController {
    Logger log = LoggerFactory.getLogger(logCheckController.class);

    @Autowired
    private IRegisterSV isv;

    @Autowired
    JedisCluster jedisCluster;

    @RequestMapping("/user/checkLogin")
    public String checkLogin(HttpServletRequest request, HttpSession session) throws Exception{
        String callback = (String)request.getParameter("callback");
        Map<String,Object> relMap = new HashMap<>();
        Map<String,Object> finalMap = new HashMap<>();
        String jsonStr = "";
        try{
            if (EmptyUtil.isEmpty(request.getParameter("username"))) {
                throw new Exception("登录失败：用户名为空");
            }
            if (EmptyUtil.isEmpty(request.getParameter("passwd"))) {
                throw new Exception("登录失败：密码为空");
            }
            relMap.put("username",request.getParameter("username"));
            relMap.put("passwd",request.getParameter("passwd"));
            Map<String,Object> teMap = isv.queryByUsername(relMap);
            if(relMap.get("passwd").equals(teMap.get("passwd"))){
                long user_id = (long) teMap.get("user_id");
                String token = KeyUtils.genUniqueKey();
                String redis_key = "TOKEN_" + user_id;
                // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
                jedisCluster.set(redis_key,token,"NX","EX",86400L);
                session.setAttribute("user",teMap);
                teMap.put("token",token);
                finalMap.put("code",0);
                finalMap.put("msg","success");
                finalMap.put("data",teMap);
                finalMap.put("count",1);
            }else{
                finalMap.put("code","-1");
                finalMap.put("msg","fail");
                finalMap.put("data","");
                finalMap.put("count",1);
            }
        }catch (Exception e){
            log.error("失败原因：",e);
            throw e;
        }

        return callback + "(" + JsonTools.object2Json(finalMap) + ")";
    }


}
