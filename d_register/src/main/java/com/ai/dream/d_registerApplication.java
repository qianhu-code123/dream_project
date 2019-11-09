package com.ai.dream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer //标识一个Eureka Server 服务注册中心
@SpringBootApplication
public class d_registerApplication {
    public static void main(String[] args) {
        SpringApplication.run(d_registerApplication.class,args);
    }
}
