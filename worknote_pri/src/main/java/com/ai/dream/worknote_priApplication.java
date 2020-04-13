package com.ai.dream;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.ai.dream.worknote.dao")
@EnableAspectJAutoProxy
public class worknote_priApplication {

    public static void main(String[] args) {
        SpringApplication.run(worknote_priApplication.class,args);
    }

}
