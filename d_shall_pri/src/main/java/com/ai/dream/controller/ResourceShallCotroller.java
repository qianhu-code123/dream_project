package com.ai.dream.controller;

import com.ai.dream.shall.service.interfaces.IResourceShallSV;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonTools;
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
public class ResourceShallCotroller {
    Logger log = LoggerFactory.getLogger(ResourceShallCotroller.class);

    @Autowired
    private IResourceShallSV isv;


    /**
     * 新增资源，显示url和名称，隐藏并用AES加密res_check[admin]
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
                relMap.put("state","U");
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
     * 查询所有资源[user][查询结果不含秘钥][用户展示]
     * @param request
     * @return
     * @throws Exception
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

    /**
     * 查询单个资源[user][查询结果包含秘钥]
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shall/res/queryResource")
    public String queryResource(HttpServletRequest request) throws Exception{
        log.info("请求参数："+request.getParameter("params"));
        Map<String,Object> finalMap = new HashMap<>();
        Map<String ,Object> relMap = new HashMap<>();
        String jsonStr = "";
        boolean flag = false;

        try {
            if(!EmptyUtil.isEmpty(request.getParameter("params"))) {
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                String userId = request.getHeader("userId");
                relMap.put("user_id",userId);
                if (EmptyUtil.isEmpty(userId)) {
                    throw new Exception("无userId信息");
                }
            }
            Map<String,Object> teMap = new HashMap<>();
            teMap.put("user_id",request.getHeader("userId"));
            Map<String,Object> resUserMap = isv.queryResUser(teMap);
            if(null == resUserMap || relMap.size() == 0){
                //首次给10次机会
                teMap.put("shall_times",9L);
                teMap.put("state","U");
                flag = isv.saveResUser(teMap);
                log.info("用户 user_id = " + request.getHeader("userId") + "初次驾到，加10次查看机会");
                if(!flag){
                    log.error("资源申请失败，请重试");
                    throw new Exception("资源申请失败，请重试");
                }
                Map<String, Object> resMap = isv.queryResource(relMap);
                relMap.put("res_check",resMap.get("RES_CHECK"));
                relMap.put("times",9L);
            }else {
                long times = (long) resUserMap.get("SHALL_TIME");
                if(times == 0){
                    log.error("用户查看权限已用尽，请联系管理员");
                    throw new Exception("用户查看权限已用尽，请联系管理员");
                }else{
                    Map<String, Object> resMap = isv.queryResource(relMap);
                    relMap.put("res_check",resMap.get("RES_CHECK"));
                    times--;
                    relMap.put("times",times);
                    isv.updateResUser(relMap);
                }
            }
            finalMap.put("code","0000");
            finalMap.put("msg","success");
            finalMap.put("data",relMap);
            finalMap.put("count",1);
        }catch (Exception e){
            log.error("失败原因：",e);
            throw e;
        }
        return JsonTools.object2Json(finalMap);
    }

    /**
     * 修改用户次数---vip权限
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shall/res/updateUT")
    public String updateUT(HttpServletRequest request) throws Exception {
        log.info("请求参数：" + request.getParameter("params"));
        Map<String, Object> finalMap = new HashMap<>();
        Map<String, Object> relMap = new HashMap<>();
        String jsonStr = "";
        boolean flag = false;
        long times = 0L;
        try{
            if(!EmptyUtil.isEmpty(request.getParameter("params"))) {
                jsonStr = request.getParameter("params");
                relMap = JsonTools.json2Map(jsonStr);
                String userId = request.getHeader("userId");
                relMap.put("user_id",userId);
                if (EmptyUtil.isEmpty(userId)) {
                    throw new Exception("无userId信息");
                }
                //level : A 表示普通 20次   B 表示中等 50次   C 表示上等 250  D 表示无限制 99999
                if (EmptyUtil.isEmpty(relMap.get("level"))) {
                    throw new Exception("修改用户次数，需提供登记");
                }
            }
            Map<String,Object> tMap = isv.queryResUser(relMap);
            if(null == tMap || 0 == tMap.size()){
                if("A".equals(relMap.get("level"))){
                    times = 20L;
                }else if("B".equals(relMap.get("level"))){
                    times = 50L;
                }else if("C".equals(relMap.get("level"))){
                    times = 250L;
                }else if("D".equals(relMap.get("level"))){
                    times = 99999L;
                }
                relMap.put("shall_times",times);
                relMap.put("state",'U');
                isv.saveResUser(relMap);
            }else{
                Map<String,Object> temp = isv.queryResUser(relMap);
                long _times_ = (long) temp.get("SHALL_TIMES");
                if("A".equals(relMap.get("level"))){
                    times = _times_ + 20L;
                }else if("B".equals(relMap.get("level"))){
                    times = _times_ + 50L;
                }else if("C".equals(relMap.get("level"))){
                    times = _times_ + 250L;
                }else if("D".equals(relMap.get("level"))){
                    times = 99999L;
                }
                relMap.put("shall_times",times);
                isv.updateResUser(relMap);
            }
            finalMap.put("code","0000");
            finalMap.put("msg","success");
            finalMap.put("data",relMap);
            finalMap.put("count",1);
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return JsonTools.object2Json(finalMap);
    }

    /**
     * 修改资源列表数据[admin]
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shall/res/updateRes")
    public String updateRes(HttpServletRequest request) throws Exception {
        log.info("请求参数：" + request.getParameter("params"));
        Map<String, Object> finalMap = new HashMap<>();
        Map<String, Object> relMap = new HashMap<>();
        String jsonStr = "";
        boolean flag = false;
        long times = 0L;
        try{
            jsonStr = request.getParameter("params");
            relMap = JsonTools.json2Map(jsonStr);
            String userId = request.getHeader("userId");
            relMap.put("user_id",userId);
            flag = isv.updateResource(relMap);
            finalMap.put("code",flag?"0000":"-1");
            finalMap.put("msg",flag?"success":"fail");
            finalMap.put("data","");
            finalMap.put("count",1);
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return JsonTools.object2Json(finalMap);
    }

}
