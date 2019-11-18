package com.ai.dream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableZuulProxy //开启ZUUL功能
@SpringBootApplication
public class d_zuul_custApplication {

    public static void main(String[] args) {
        SpringApplication.run(d_zuul_custApplication.class,args);
    }

}
