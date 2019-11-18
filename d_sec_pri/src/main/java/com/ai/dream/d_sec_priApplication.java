package com.ai.dream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class d_sec_priApplication {
    public static void main(String[] args) {
        SpringApplication.run(d_sec_priApplication.class,args);
    }

}
