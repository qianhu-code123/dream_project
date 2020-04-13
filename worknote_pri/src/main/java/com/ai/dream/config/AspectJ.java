package com.ai.dream.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;



@Aspect
@Component
@Slf4j
public class AspectJ {

    @Pointcut("execution(*  com.ai.dream.worknote.services.*.*(..)))")   //the point expression
    private void pointcut(){}

    @Around("com.ai.dream.config.AspectJ.pointcut()")
    public Object around(ProceedingJoinPoint pjp){
        long start = System.currentTimeMillis();
        Object proceed = null;
        try {
            proceed = pjp.proceed();
        }catch (Throwable throwable) {
            throwable.printStackTrace();
        }
         long end = System.currentTimeMillis();
         log.info("接口耗时："+(end-start));

         return proceed;
    }


}
