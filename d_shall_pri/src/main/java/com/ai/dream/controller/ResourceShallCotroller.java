package com.ai.dream.controller;

import com.ai.dream.shall.service.interfaces.IResourceShallSV;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonInit;
import com.ai.dream.utils.JsonTools;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public String save(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        Map<String ,Object> relMap = new HashMap<>();
        String jsonStr = "";
        boolean flag;
        try {
            if (EmptyUtil.isEmpty(request.getParameter("res_name"))) {
                throw new Exception("资源名称为空!");
            }
            if (EmptyUtil.isEmpty(request.getParameter("res_url"))) {
                throw new Exception("资源链接为空!");
            }
            if (EmptyUtil.isEmpty(request.getParameter("res_check"))) {
                throw new Exception("资源提取码名称为空!");
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
            String start_date = format.format(new Date());
            relMap.put("res_id",request.getParameter("res_id"));
            relMap.put("res_name",request.getParameter("res_name"));
            relMap.put("res_url",request.getParameter("res_url"));
            relMap.put("res_check",request.getParameter("res_check"));
            relMap.put("create_date",start_date);
            relMap.put("expire_date","2099-12-31 23:59:59");
            relMap.put("state","U");
            flag = isv.saveResource(relMap);
            jsonStr = JsonInit.rebakJson(flag?"0000":"-1",flag?"success":"fail","",1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonStr;
    }

    /**
     * 查询所有资源[user][查询结果不含秘钥][用户展示]
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shall/res/queryAllResource")
    public String queryAllRes(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Map<String,Object>> relist = new ArrayList<>();
        String jsonStr = "";
        try {
            String page = request.getParameter("page");
            String limit = request.getParameter("limit");
            String res_id = request.getParameter("res_id")== null ? "":request.getParameter("res_id");
            String res_name = request.getParameter("res_name")== null ? "":request.getParameter("res_name");
            Map<String,Object> temMap = new HashMap<>();
            temMap.put("res_id",res_id);
            temMap.put("res_name",res_name);
            long start = Long.parseLong(page) * Long.parseLong(limit) - Long.parseLong(limit);
            long count = Long.parseLong(limit);
            temMap.put("start",start);
            temMap.put("count",count);
            //page=1 limit 10 limit limit*page-limit,limit*page  0 10
            //page=2 limit 10 limit limit*page-limit,limit*page  10 20
            //page=3 limit 10 limit limit*page-limit ,limit*page 20 30
            List<Map<String,Object>> list = isv.queryAllResource(temMap);
            Iterator it = list.iterator();
            while(it.hasNext()){
                Map<String,Object> temp = (Map<String, Object>) it.next();
                temp.put("state","U".equals(temp.get("state"))?"有效":"无效");
                temp.put("expire_date","2099-12-31 23:59:59");
                temp.put("res_check","<span onclick='getTicket("+temp.get("res_id")+")' style='color:green'>点击查询<span>");
                relist.add(temp);
            }
            jsonStr = JsonInit.rebakJson("0000","查询成功",relist,relist.size());
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因：",e);
            throw e;
        }
        return jsonStr;
    }

    /**
     * 查询单个资源[user][查询结果包含秘钥]
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shall/res/queryResource")
    public String queryResource(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String,Object> finalMap = new HashMap<>();
        Map<String ,Object> relMap = new HashMap<>();
        String jsonStr = "";
        boolean flag;
        try {
            String res_id = request.getParameter("res_id");
            String userId = request.getParameter("user_id");
            relMap.put("res_id",res_id);
            relMap.put("user_id",userId);
            if (EmptyUtil.isEmpty(userId)) {
                throw new Exception("无userId信息");
            }
            Map<String,Object> resUserMap = isv.queryResUser(relMap);
            if(null == resUserMap || resUserMap.size() == 0){
                //首次给10次机会
                relMap.put("shall_times",9L);
                relMap.put("state","U");
                flag = isv.saveResUser(relMap);
                log.info("用户 user_id = " + request.getHeader("user_id") + "初次驾到，加10次查看机会");
                if(!flag){
                    log.error("资源申请失败，请重试");
                    throw new Exception("资源申请失败，请重试");
                }
                Map<String, Object> resMap = isv.queryResource(relMap);
                relMap.put("res_check",resMap.get("res_check"));
                relMap.put("times",9L);
            }else {
                int times = (int) resUserMap.get("shall_times");
                if(times == 0){
                    log.error("用户查看权限已用尽，请联系管理员");
                    jsonStr = JsonInit.rebakJson("1000","fail","",0);
                    response.setHeader("Access-Control-Allow-Origin","*");
                    return jsonStr;
                }else{
                    Map<String, Object> resMap = isv.queryResource(relMap);
                    relMap.put("res_check",resMap.get("res_check"));
                    times--;
                    relMap.put("times",times);
                    relMap.put("state","U");
                    isv.updateResUser(relMap);
                }
            }
            jsonStr = JsonInit.rebakJson("0000","success",relMap,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因：",e);
            throw e;
        }
        return jsonStr;
    }

    /**
     * 查询用户拥有的次数
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shall/res/queryResUser")
    public String queryResUser(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String ,Object> relMap = new HashMap<>();
        String jsonStr = "";
        boolean flag;
        try {
            String userId = request.getParameter("userId");
            relMap.put("user_id",userId);
            Map<String,Object> resUserMap = isv.queryResUser(relMap);
            relMap.put("times",resUserMap.get("shall_times"));
            jsonStr = JsonInit.rebakJson("0000","查询用户拥有的次数",relMap,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因：",e);
            throw e;
        }
        return jsonStr;
    }

    /**
     * 修改用户次数---vip权限
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shall/res/updateUT")
    public String updateUT(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String, Object> finalMap = new HashMap<>();
        Map<String, Object> relMap = new HashMap<>();
        String jsonStr = "";
        try{
            String userId = request.getParameter("user_id");
            long times = Long.parseLong(request.getParameter("times"));
            relMap.put("user_id",userId);
            relMap.put("times",times);
            relMap.put("state","U");
            isv.updateResUser(relMap);
            jsonStr = JsonInit.rebakJson("0000","success",relMap,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonStr;
    }

    /**
     * 修改资源列表数据[admin]
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shall/res/updateRes")
    public String updateRes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> finalMap = new HashMap<>();
        Map<String, Object> relMap = new HashMap<>();
        String jsonStr = "";
        try{
            String res_id = request.getParameter("res_id");
            String res_check = request.getParameter("res_check");
            String state = request.getParameter("state");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
            String start_date = format.format(new Date());
            relMap.put("res_id",start_date);
            relMap.put("state",state);
            isv.updateResource(relMap);
            jsonStr = JsonInit.rebakJson("0000","success",relMap,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonStr;
    }

}
