package com.ai.dream.controller;

import com.ai.dream.config.HelperCount;
import com.ai.dream.shall.entity.ResourceShall;
import com.ai.dream.shall.service.interfaces.IResourceShallSV;
import com.ai.dream.utils.DateUtils;
import com.ai.dream.utils.EmptyUtil;
import com.ai.dream.utils.JsonInit;
import com.ai.dream.utils.JsonTools;
import com.sun.org.apache.xalan.internal.res.XSLTErrorResources;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ResourceShallCotroller {
    Logger log = LoggerFactory.getLogger(ResourceShallCotroller.class);

    @Autowired
    private IResourceShallSV isv;

    @Autowired
    private JedisCluster jedisCluster;
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
            Map<String,Object> temMap = isv.queryResource(relMap);
            if(null != temMap && temMap.size() > 0){
                throw new Exception("资源id："+request.getParameter("res_id")+"已经存在，请勿重复添加");
            }
            relMap.put("res_name",request.getParameter("res_name"));
            relMap.put("res_url",request.getParameter("res_url"));
            relMap.put("res_check",request.getParameter("res_check"));
            relMap.put("create_date",start_date);
            relMap.put("expire_date","2099-12-31 23:59:59");
            relMap.put("state","U");
            relMap.put("level",request.getParameter("level"));
            flag = isv.saveResource(relMap);
            jsonStr = JsonInit.rebakJson(flag?"0000":"-1",flag?"success":"fail","",1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因 ",e);
            jsonStr = JsonInit.rebakJson("-1",e.getMessage(),"",0);
            response.setHeader("Access-Control-Allow-Origin","*");
            return jsonStr;
        }
        return jsonStr;
    }

    @RequestMapping("/shall/res/saveFromExcel")
    public String saveMore(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws  Exception{
         List<ResourceShall> listshall = new ArrayList<>();
         Map<String,Object> relMap = new HashMap<>();
         InputStream input = null;
         String jsonStr = "";
         XSSFWorkbook wb = null;
         boolean flag ;
        try {
            input = file.getInputStream();
            wb = new XSSFWorkbook(input);
            //读取页
            for(int sheetNum=0;sheetNum<wb.getNumberOfSheets();sheetNum++){
                XSSFSheet xssfSheet = wb.getSheetAt(sheetNum);
                if(xssfSheet==null){
                    //遇到空行跳出循环
                    continue;
                }
                //读取行
                for(int rowNum=1;rowNum<xssfSheet.getLastRowNum()+1;rowNum++){
                    ResourceShall res = new ResourceShall();
                    XSSFRow row= xssfSheet.getRow(rowNum);
                    if("".equals(row.getCell(0)) || null == row.getCell(0)){
                        break;
                    }
                    if(row!=null ){
                        for(int cellNum=0;cellNum<row.getLastCellNum();cellNum++){
                            if(cellNum == 0){
                                if(row.getCell(cellNum) == null){
                                    break;
                                }
                                Map<String,Object> temMap = new HashMap<>();
                                res.setResId(Long.parseLong(row.getCell(cellNum).toString().replace(".0","")));
                                temMap.put("res_id",Long.parseLong(row.getCell(cellNum).toString().replace(".0","")));
                                Map<String,Object> listMap = isv.queryResource(temMap);
                                if(null!=listMap && listMap.size() > 0 ){
                                    throw new Exception("资源id："+row.getCell(cellNum).toString().replace(".0","")+"已存在");
                                }
                            }else if(cellNum == 1){
                                res.setResName(row.getCell(cellNum).toString());
                            }else if(cellNum == 2){
                                res.setResUrl(row.getCell(cellNum).toString());
                            }else if(cellNum == 3){
                                res.setResCheck(row.getCell(cellNum).toString());
                            }else if (cellNum == 4){
                                //将poi解析的excel中date转换成固定格式
                                res.setCreateDate(DateUtils.getExcelDate(row.getCell(cellNum).toString()));
                            }else if(cellNum == 5){
                                //将poi解析的excel中date转换成固定格式
                                res.setExpireDate(DateUtils.getExcelDate(row.getCell(cellNum).toString()));
                            }else if(cellNum == 6){
                                res.setState(row.getCell(cellNum).toString());
                            }else if(cellNum == 7){
                                res.setLevel(row.getCell(cellNum).toString());
                            }
                        }
                    }
                    listshall.add(res);
                }
            }
            flag = isv.saveFromExcel(listshall);
            jsonStr = JsonInit.rebakJson(flag?"0000":"-1",flag?"success":"fail","",listshall.size());
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因 ",e);
            jsonStr = JsonInit.rebakJson("-1",e.getMessage(),"",0);
            response.setHeader("Access-Control-Allow-Origin","*");
            return jsonStr;
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
            temMap.put("level",request.getParameter("type"));
            List<Map<String,Object>> list = isv.queryAllResource(temMap);
            Iterator it = list.iterator();
            while(it.hasNext()){
                Map<String,Object> temp = (Map<String, Object>) it.next();
                temp.put("state","U".equals(temp.get("state"))?"有效":"无效");
                temp.put("expire_date","2099-12-31 23:59:59");
                temp.put("res_check","<span onclick='getTicket("+temp.get("res_id")+")' style='color:green'>点击查询<span>");
                temp.put("level", HelperCount.getString(temp.get("level").toString()));
                relist.add(temp);
            }
            jsonStr = JsonInit.rebakJson("0000","查询成功",relist,relist.size());
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因：",e);
            jsonStr = JsonInit.rebakJson("-1",e.getMessage(),"",0);
            response.setHeader("Access-Control-Allow-Origin","*");
            return jsonStr;
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
            //根据res_id查看资源等级
            Map<String,Object> resMap = isv.queryResource(relMap);
            String level = resMap.get("level").toString();
            String res_check = resMap.get("res_check").toString();
            Map<String,Object> resUserMap = isv.queryResUser(relMap); //根据里面的user_id查询
            if(null == resUserMap || 0 == resUserMap.size()){
                log.info("初次使用，赋予10次查看普通资源权限");
                Map<String,Object> newUserMap = new HashMap<>();
                newUserMap.put("user_id",userId);
                newUserMap.put("shall_times",10L);
                newUserMap.put("vip_times",0);
                newUserMap.put("state","U");
                isv.saveResUser(newUserMap);
            }
            resUserMap = isv.queryResUser(relMap);
            int vip =resUserMap.get("vip_times")==null?0:(int) resUserMap.get("vip_times") ;
            int novip = resUserMap.get("shall_times")==null?0:(int) resUserMap.get("shall_times") ;;
            //如果是VIP资源,先查vip次数
            if(level.contains("V")){
                if(vip <= 0){
                    throw new Exception("次数不够1次，请申请更多vip查看权限");
                }else {
                    vip--;
                    finalMap.put("user_id",userId);
                    finalMap.put("res_check",res_check);
                    finalMap.put("vip_times",vip);
                    finalMap.put("shall_times",novip);
                    finalMap.put("state","U");
                    isv.updateResUser(finalMap);//更新最新次数
                }
            }else {
                if(novip <= 0){
                    throw new Exception("次数不够1次，请申请更多普通资源查看权限");
                }else {
                    novip--;
                    finalMap.put("user_id",userId);
                    finalMap.put("res_check",res_check);
                    finalMap.put("vip_times",vip);
                    finalMap.put("shall_times",novip);
                    finalMap.put("state","U");
                    isv.updateResUser(finalMap);
                }
            }
            jsonStr = JsonInit.rebakJson("0000","success",finalMap,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因：",e);
            jsonStr = JsonInit.rebakJson("-1",e.getMessage(),"",1);
            response.setHeader("Access-Control-Allow-Origin","*");
            return jsonStr;
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
            log.error("失败原因 ",e);
            jsonStr = JsonInit.rebakJson("-1",e.getMessage(),"",0);
            response.setHeader("Access-Control-Allow-Origin","*");
            return jsonStr;
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
            long vipTimes = Long.parseLong(request.getParameter("vip_times"));
            relMap.put("user_id",userId);
            relMap.put("times",times);
            relMap.put("vip_times",vipTimes);
            relMap.put("state","U");
            boolean flag = isv.updateResUser(relMap);
            jsonStr = JsonInit.rebakJson(flag?"0000":"-1",flag?"success":"fail",relMap,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因 ",e);
            jsonStr = JsonInit.rebakJson("-1",e.getMessage(),"",0);
            response.setHeader("Access-Control-Allow-Origin","*");
            return jsonStr;
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
            String res_name = request.getParameter("res_name");
            String res_url = request.getParameter("res_url");
            String level = request.getParameter("level");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
            String start_date = format.format(new Date());
            relMap.put("create_date",start_date);
            relMap.put("res_check",res_check);
            relMap.put("res_name",res_name);
            relMap.put("res_url",res_url);
            relMap.put("res_id",res_id);
            relMap.put("level",level);
            relMap.put("state",state);
            boolean flag = isv.updateResource(relMap);
            jsonStr = JsonInit.rebakJson(flag?"0000":"-1",flag?"success":"fail",relMap,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因 ",e);
            jsonStr = JsonInit.rebakJson("-1",e.getMessage(),"",0);
            response.setHeader("Access-Control-Allow-Origin","*");
            return jsonStr;
        }
        return jsonStr;
    }

    @RequestMapping("/shall/res/delByResId")
    public String delByResId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> relMap = new HashMap<>();
        String jsonStr = "";
        try{
            String mobileStr = request.getParameter("data");
            List<Map<String,Object>> mobileItem = JsonTools.json2List(mobileStr);
            List<String> item = new ArrayList<>();
            for(int i=0;i<mobileItem.size();i++){
                item.add(mobileItem.get(i).get("res_id").toString());
            }
            boolean flag = isv.delResById(item);
            jsonStr = JsonInit.rebakJson(flag?"0000":"-1",flag?"success":"fail",relMap,1);
            response.setHeader("Access-Control-Allow-Origin","*");
        }catch (Exception e){
            log.error("失败原因 ",e);
            jsonStr = JsonInit.rebakJson("-1",e.getMessage(),"",0);
            response.setHeader("Access-Control-Allow-Origin","*");
            return jsonStr;
        }
        return jsonStr;
    }

    /**
     * 多个用户抢一个商品
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getOnlyGoods")
    public String getOnlyGoods(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //uid表示当前的线程id
        String uid = "";
        try {
            uid = String.valueOf(Thread.currentThread().getId());
            if("OK".equals(jedisCluster.set("lock",uid,"NX","EX",30))){
                log.info("-------------线程["+Thread.currentThread().getName()+"]抢到资源，30秒后过期--------------");
                //支付接口
                //成功支付后，库存广播,终止购买同时释放锁
                jedisCluster.del("lock");
            }
        }catch (Exception e){
            log.error("失败原因",e.getMessage());
        }
        return null;
    }
    /**
     * 多个用户抢有限个商品100个
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getMoreGoods")
    public String getMoreGoods(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //uid表示当前的线程id
        String uid = "";
        try {
            //获取最新库存数,100
            int staticCount =Integer.parseInt(jedisCluster.get("staticCount"));
            uid = Thread.currentThread().getName();
            for(int i=0;i<staticCount;i++){
                if("OK".equals(jedisCluster.set("lock_"+i,uid,"NX","EX",5))){
                    log.info("线程"+uid+"正抢到资源lock_"+i+"......正准备支付---------------------------------------");
                    long flag =  Math.round(Math.random()*10);
                    if(flag == 1){
                        //假设这个场景，表示用户不想买了
                        log.info("用户"+uid+"放弃购买");
                        jedisCluster.del("lock_"+i);
                    }else if(flag == 3){
                        //假设该场景表示用户操作时间过长，释放锁
                        log.info("用户："+uid+"即将超时");
                        Thread.sleep(5000);
                    }else {
                        //购买成功,更新库存，设置库存锁的key是public_kuc 30s过期
                        if("OK".equals(jedisCluster.set("public_kuc",uid,"NX","EX",5))){
                            int redcount = Integer.parseInt(jedisCluster.get("redBag"));
                            redcount--;
                            log.info("用户:"+uid+"购买，正在更新库存，当前还剩余"+redcount);
                            //操作完成后给这个物品永久加锁
                            jedisCluster.set("lock_"+i,"ok");
                            jedisCluster.set("redBag",String.valueOf(redcount));
                            jedisCluster.del("public_kuc");
                        }
                    }
                    break;
                }else {
                    log.error("lock_"+i+"已经售完");
                }
            }
        }catch (Exception e){
            log.error("失败原因"+e.getMessage());
        }
        return null;
    }




}
