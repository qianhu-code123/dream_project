package com.ai.dream.controller;

import com.ai.dream.sec.service.interfaces.IRegisterSV;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonTools;
import com.ai.dream.utils.SequenceUtil;
import org.json.JSONObject;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class registerController {

    Logger log = LoggerFactory.getLogger(registerController.class);

    @Autowired
    IRegisterSV isv;

    @RequestMapping("/user/saveUser")
    public String saveUser(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        Map<String,Object> finalMap = new HashMap<>();
        boolean flag = false;
        try {
            if(!EmptyUtil.isEmpty(request.getParameter("params"))){
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                if(EmptyUtil.isEmpty(relMap.get("username"))){
                    throw new Exception("用户名不能为空");
                }
                if(EmptyUtil.isEmpty(relMap.get("passwd"))){
                    throw new Exception("密码不能为空");
                }
                if(EmptyUtil.isEmpty(relMap.get("mobile"))){
                    throw new Exception("手机号码不能为空");
                }
                relMap.put("mobile",relMap.get("mobile").toString());
                Map<String,Object> userMap = isv.queryUser(relMap);
                if(null!=userMap && userMap.size() > 0){
                    throw new Exception("手机号码已绑定用户，请更换手机号码!");
                }
                relMap.put("user_id",Long.parseLong(SequenceUtil.getSeq()));
                relMap.put("username",relMap.get("username").toString());
                relMap.put("passwd",relMap.get("passwd").toString());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
                java.util.Date uDate = new Date();
                String str = format.format(uDate);
                uDate = format.parse(str);
                relMap.put("create_date",uDate);
                relMap.put("expire_date",format.parse("2099-12-31 00:00:00"));
                relMap.put("state","U");
                flag = isv.saveUser(relMap);
                finalMap.put("code",flag?"0000":"-1");
                finalMap.put("msg",flag?"注册成功":"失败");
                finalMap.put("data","成功");
                finalMap.put("count",1);
                jsonStr = JsonTools.object2Json(finalMap);
            }
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonStr;
    }

    @RequestMapping("/user/delUser")
    public String delUser(HttpServletRequest request) throws Exception{
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        Map<String,Object> finalMap = new HashMap<>();
        boolean flag = false;
        try {
            if(!EmptyUtil.isEmpty(request.getParameter("params"))){
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
            }
            if(EmptyUtil.isEmpty(relMap.get("mobile"))){
                throw new Exception("mobile为空");
            }
            relMap.put("mobile",relMap.get("mobile"));
            flag = isv.deleteUser(relMap);
            finalMap.put("code",flag ?"0000":"-1");
            finalMap.put("msg",flag?"删除成功":"失败");
            finalMap.put("data","");
            finalMap.put("count",1);
            jsonStr = JsonTools.object2Json(finalMap);
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonStr;
    }


    @RequestMapping("/user/queryAll")
    public String queryAll(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        Map<String,Object> finalMap = new HashMap<>();
        String jsonStr = "";
        try{
            List<Map<String,Object>> rel = isv.queryAllUser();
            if(null == rel || rel.size() == 0){

                throw new Exception("查询失败，信息为空");
            }
            finalMap.put("code","0000");
            finalMap.put("msg","查询成功");
            finalMap.put("data",rel);
            finalMap.put("count",rel.size());
            jsonStr = JsonTools.object2Json(finalMap);
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonStr;
    }


    @RequestMapping("/user/queryUser")
    public String queryUser(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        Map<String,Object> finalMap = new HashMap<>();
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        boolean flag = true;
        try{
            if(!EmptyUtil.isEmpty(request.getParameter("params"))) {
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                if (EmptyUtil.isEmpty(relMap.get("mobile"))) {
                    throw new Exception("查询失败：mobile为空");
                }
            }
            relMap.put("mobile",relMap.get("mobile"));
            Map<String,Object> rel = isv.queryUser(relMap);
            if(null == rel || rel.size() == 0){
                flag = false;
                throw new Exception("查询失败，信息为空");
            }
            finalMap.put("code","0000");
            finalMap.put("msg","查询成功");
            finalMap.put("data",rel);
            finalMap.put("count",1);
            jsonStr = JsonTools.object2Json(finalMap);
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonStr;
    }

}
