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
import java.util.HashMap;
import java.util.Map;

@RestController
public class logCheckController {
    Logger log = LoggerFactory.getLogger(logCheckController.class);

    @Autowired
    private IRegisterSV isv;

    @Autowired
    JedisCluster jedisCluster;

    @RequestMapping("/login/checkLogin")
    public String checkLogin(HttpServletRequest request) throws Exception{
        Map<String,Object> relMap = new HashMap<>();
        Map<String,Object> finalMap = new HashMap<>();
        String jsonStr = "";
        try{
            if(!EmptyUtil.isEmpty(request.getParameter("params"))) {
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                if (EmptyUtil.isEmpty(relMap.get("username"))) {
                    throw new Exception("登录失败：用户名为空");
                }
                if (EmptyUtil.isEmpty(relMap.get("passwd"))) {
                    throw new Exception("登录失败：密码为空");
                }
            }
            Map<String,Object> teMap = isv.queryByUsername(relMap);
            if(relMap.get("passwd").equals(teMap.get("passwd"))){
                long user_id = (long) teMap.get("user_id");
                String token = KeyUtils.genUniqueKey();
                String redis_key = "TOKEN_" + user_id;
                jedisCluster.set(redis_key,token);
                finalMap.put("code","0000");
                finalMap.put("msg","登录成功");
                finalMap.put("data",token);
                finalMap.put("count",1);
            }else{
                finalMap.put("code","-1");
                finalMap.put("msg","用户名密码不匹配");
                finalMap.put("data","");
                finalMap.put("count",1);
            }
        }catch (Exception e){
            log.error("失败原因：",e);
            throw e;
        }
        return JsonTools.object2Json(finalMap);
    }


}
