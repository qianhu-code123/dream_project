package com.ai.dream.controller;

import com.ai.dream.shall.service.interfaces.IResourceShallSV;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonTools;
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
public class ResourceShallCotroller {
    Logger log = LoggerFactory.getLogger(ResourceShallCotroller.class);

    @Autowired
    private IResourceShallSV isv;


    /**
     * 新增资源，显示url和名称，隐藏并用AES加密res_check
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shall/res/save")
    public String save(HttpServletRequest request) throws  Exception{
        log.info("请求参数："+request.getParameter("params"));
        Map<String ,Object> relMap = new HashMap<>();
        Map<String,Object> finalMap = new HashMap<>();
        String jsonStr = "";
        boolean flag = false;
        try {
            if(!EmptyUtil.isEmpty(request.getParameter("params"))) {
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                if (EmptyUtil.isEmpty(relMap.get("res_name"))) {
                    throw new Exception("资源名称为空!");
                }
                if (EmptyUtil.isEmpty(relMap.get("res_url"))) {
                    throw new Exception("资源链接为空!");
                }
                if (EmptyUtil.isEmpty(relMap.get("res_check"))) {
                    throw new Exception("资源提取码名称为空!");
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
                java.util.Date create_date = format.parse(relMap.get("create_date").toString());
                java.util.Date expire_date = format.parse(relMap.get("expire_date").toString());
                relMap.put("create_date",create_date);
                relMap.put("expire_date",expire_date);
                flag = isv.saveResource(relMap);

                finalMap.put("code",flag?"0000":"-1");
                finalMap.put("msg",flag?"success":"fail");
                finalMap.put("data","");
                finalMap.put("count",flag?1:0);
            }

        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return JsonTools.object2Json(finalMap);
    }

    /**
     *
     */
    @RequestMapping("/shall/res/queryAllResource")
    public String queryAllRes(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        Map<String,Object> finalMap = new HashMap<>();
        boolean flag = false;
        try {
            List<Map<String,Object>> list = isv.queryAllResource();
            if(null == list || 0 == list.size()){
                flag = false;
            }else{
                flag =true;
            }
            finalMap.put("code",flag?"0000":"-1");
            finalMap.put("msg",flag?"success":"fail");
            finalMap.put("data",list);
            finalMap.put("count",flag?list.size():0);
        }catch (Exception e){
            log.error("失败原因：",e);
            throw e;
        }
        return JsonTools.object2Json(finalMap);
    }

    @RequestMapping("/shall/res/queryResource")
    public String queryResource(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        Map<String,Object> finalMap = new HashMap<>();
        Map<String ,Object> relMap = new HashMap<>();
        //Map<String,Object> finalMap = new HashMap<>();
        String jsonStr = "";
        boolean flag = false;

        try {
            if(!EmptyUtil.isEmpty(request.getParameter("params"))) {
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                String userId = request.getHeader("userId");
                if (EmptyUtil.isEmpty(relMap.get("userId"))) {
                    throw new Exception("头部无userId信息");
                }
            }
            Map<String,Object> teMap = new HashMap<>();
            teMap.put("user_id",request.getHeader("userId"));
            Map<String,Object> resUserMap = isv.queryResUser(teMap);
            if(null == resUserMap || relMap.size() == 0){
                teMap.put("shall_time",10l);
                teMap.put("state","U");
                flag = isv.saveResUser(teMap);
                log.info("用户 user_id = " + request.getHeader("userId") + "初次驾到，加10次查看机会");
            }
            if(flag){

            }
            finalMap.put("code",flag?"0000":"-1");
            finalMap.put("msg",flag?"success":"fail");
            //finalMap.put("data",list);
            //finalMap.put("count",flag?list.size():0);
        }catch (Exception e){
            log.error("失败原因：",e);
            throw e;
        }
        return JsonTools.object2Json(finalMap);
    }


}
