package com.ai.dream;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient //将此服务注册到Eureka 服务注册中心
@MapperScan("com.ai.dream.mapper")
@SpringBootApplication
public class d_collect_priApplication {
    public static void main(String[] args) {
        SpringApplication.run(d_collect_priApplication.class,args);
    }

}
