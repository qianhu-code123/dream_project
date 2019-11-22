package com.ai.dream;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.ai.dream.shall.mapper")
public class d_shall_priApplication {

    public static void main(String[] args) {

        SpringApplication.run(d_shall_priApplication.class,args);
    }

}
