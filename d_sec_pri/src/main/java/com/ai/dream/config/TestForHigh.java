package com.ai.dream.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CountDownLatch;

@Controller
public class TestForHigh {

    Logger log = LoggerFactory.getLogger(TestForHigh.class);


    @RequestMapping("/test")
    public String test(HttpServletRequest request){
        int ThredNum = Integer.parseInt(request.getParameter("number"));
        try{
            final long time = System.currentTimeMillis();
            final String url = "http://localhost:9002/getMoreGoods";

            final CountDownLatch latch = new CountDownLatch(ThredNum);
            for (int i = 0; i < ThredNum; i++) {
                final int id = i;
                new Thread(new Runnable() {
                    public void run() {
                        latch.countDown();
                        RestTemplate template = new RestTemplate();
                        template.getForObject(url,String.class);
                        long usetime = System.currentTimeMillis() - time;
                        log.info("到第" + id + "个请求已用时：" + usetime / 1000 + "秒");
                    }
                }).start();
            }
            latch.await();
        }catch (Exception e){

        }
        return null;
    }


}
