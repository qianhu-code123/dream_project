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
    public JSONObject saveUser(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        JSONObject jsonObject = new JSONObject();
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
                relMap.put("mobile",relMap.get("mobile"));
                Map<String,Object> userMap = isv.queryUser(relMap);
                if(null!=userMap && userMap.size() > 0){
                    throw new Exception("用户已存在!");
                }
                relMap.put("user_id",Long.parseLong(SequenceUtil.getSeq()));
                relMap.put("username",relMap.get("username").toString());
                relMap.put("passwd",relMap.get("mobile").toString());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:si:mm");
                java.util.Date uDate = new Date();
                String str = format.format(uDate);
                java.sql.Timestamp timestamp = new java.sql.Timestamp(format.parse(str).getTime());
                relMap.put("create_date",timestamp);
                relMap.put("expire_date",new java.sql.Timestamp(format.parse("2099-12-12 00:00:00").getTime()));
                relMap.put("state","U");
                flag = isv.saveUser(relMap);
                jsonObject.put("code",flag == true ?"0000":"-1");
                jsonObject.put("msg",flag == true?"注册成功":"失败");
                jsonObject.put("data","成功");
                jsonObject.put("count",1);
            }
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonObject;
    }

    @RequestMapping("/user/delUser")
    public JSONObject delUser(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        try{


        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }



        return null;
    }


    @RequestMapping("/user/queryAll")
    public JSONObject queryAll(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        JSONObject jsonObject = new JSONObject();
        boolean flag = true;
        try{
            List<Map<String,Object>> rel = isv.queryAllUser();
            if(null == rel || rel.size() == 0){
                flag = false;
                throw new Exception("查询失败，信息为空");
            }
            jsonObject.put("code",flag == true ?"0000":"-1");
            jsonObject.put("msg",flag == true?"查询成功":"失败");
            jsonObject.put("data",rel);
            jsonObject.put("count",rel.size());
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonObject;
    }


    @RequestMapping("/user/queryUser")
    public JSONObject queryUser(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        JSONObject jsonObject = new JSONObject();
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
            jsonObject.put("code",flag == true ?"0000":"-1");
            jsonObject.put("msg",flag == true?"查询成功":"失败");
            jsonObject.put("data",rel);
            jsonObject.put("count",rel.size());
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonObject;
    }

}
