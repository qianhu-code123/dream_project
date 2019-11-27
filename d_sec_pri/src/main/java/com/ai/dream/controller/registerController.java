package com.ai.dream.controller;

import com.ai.dream.sec.service.interfaces.IRegisterSV;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonInit;
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
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class registerController {

    Logger log = LoggerFactory.getLogger(registerController.class);

    @Autowired
    IRegisterSV isv;

    @RequestMapping("/user/saveUser")
    public String saveUser(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        boolean flag = false;
        try {
            if(EmptyUtil.isEmpty(request.getParameter("username"))){
                throw new Exception("用户名不能为空");
            }
            if(EmptyUtil.isEmpty(request.getParameter("passwd"))){
                throw new Exception("密码不能为空");
            }
            if(EmptyUtil.isEmpty(request.getParameter("mobile"))){
                throw new Exception("手机号码不能为空");
            }
            relMap.put("mobile",request.getParameter("mobile"));
            Map<String,Object> userMap = isv.queryUser(relMap);
            if(null!=userMap && userMap.size() > 0){
                throw new Exception("手机号码已绑定用户，请更换手机号码!");
            }
            relMap.put("user_id",Long.parseLong(SequenceUtil.getSeq()));
            relMap.put("username",request.getParameter("username"));
            relMap.put("passwd",request.getParameter("passwd"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
            java.util.Date uDate = new Date();
            String str = format.format(uDate);
            uDate = format.parse(str);
            relMap.put("create_date",uDate);
            relMap.put("expire_date",format.parse("2099-12-31 23:59:59"));
            relMap.put("state","U");
            flag = isv.saveUser(relMap);
            jsonStr = JsonInit.rebakJson(flag?"0000":"-1",flag?"success":"fail","",1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonStr;
    }

    @RequestMapping("/user/delUser")
    public String delUser(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String jsonStr = "";
        boolean flag = false;
        try {
            if(EmptyUtil.isEmpty(request.getParameter("data"))){
                throw new Exception("data为空");
            }
            String mobileStr = request.getParameter("data");
            List<Map<String,Object>> mobileItem = JsonTools.json2List(mobileStr);
            List<String> item = new ArrayList<>();
            for(int i=0;i<mobileItem.size();i++){
                item.add(mobileItem.get(i).get("mobile").toString());
            }
            flag = isv.deleteUser(item);
            jsonStr = JsonInit.rebakJson(flag ?"0000":"-1",flag?"删除成功":"删除失败","",item.size());
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return jsonStr;
    }


    @RequestMapping("/user/queryAll")
    public String queryAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        try{
            String username = request.getParameter("username")== null ? "":request.getParameter("username");
            String mobile = request.getParameter("mobile")== null ? "":request.getParameter("mobile");
            relMap.put("username",username);
            relMap.put("mobile",mobile);
            List<Map<String,Object>> rel = isv.queryAllUser(relMap);
            if(null == rel || rel.size() == 0){
                log.info("查询信息为空");
                response.setHeader("Access-Control-Allow-Origin","*");
                return JsonInit.rebakJson("-1","未查询到信息",null,0);
            }
            for(int i=0;i<rel.size();i++){
                Map<String,Object> temp = rel.get(i);
                String createStr =  temp.get("create_date").toString().substring(0,19);
                String endStr = temp.get("expire_date").toString().substring(0,19);
                temp.put("create_date",createStr);
                temp.put("expire_date",endStr);
                list.add(temp);
            }
            jsonStr = JsonInit.rebakJson("0000","查询成功",list,list.size());
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因： ",e);
            throw e;
        }
        return  jsonStr;
    }

    /**
     * 1.修改密码，无限制
     * 2.更换手机号码，需要判断更换后的手机号是否被占用
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/user/updateUser")
    public String updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> relMap = new HashMap<>();
        String jsonStr = "";
        boolean flag = false;
        try {
            String user_id = request.getParameter("userItemId")== null ? "": request.getParameter("userItemId");
            String passwd = request.getParameter("passwd")== null ? "": request.getParameter("passwd");
            //更换后的手机
            String newMobile = request.getParameter("mobile")== null ? "": request.getParameter("mobile");
            relMap.put("mobile",newMobile);
            relMap.put("passwd",passwd);
            relMap.put("user_id",user_id);
            Map<String,Object> tem = isv.queryUser(relMap);
            if(null == tem || 0 == tem.size() || user_id == tem.get("user_id").toString()){
                //号码未被占用/号码未修改，可以修改
                flag = isv.updateUser(relMap);
                response.setHeader("Access-Control-Allow-Origin","*");
                return JsonInit.rebakJson(flag?"0000":"-1",flag?"修改成功":"修改失败",null,1);
            }
            //号码被其他用户占用
            response.setHeader("Access-Control-Allow-Origin","*");
            return JsonInit.rebakJson("-1","手机号码已被其他用户占用",null,0);
        }catch (Exception e){
            log.error("失败原因 ",e);
            throw e;
        }
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
            finalMap.put("msg","success");
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
