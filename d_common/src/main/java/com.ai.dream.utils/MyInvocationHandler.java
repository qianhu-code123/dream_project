package com.ai.dream.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingDeque;

public class MyInvocationHandler implements InvocationHandler {

    private BLforMap bLforMap;

    public MyInvocationHandler(){
        super();
    }

    public MyInvocationHandler(BLforMap bLforMap){
        super();
        this.bLforMap=bLforMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("------------------------------");
        method.invoke(bLforMap,args);
        System.out.println("------------------------------");
        return null;
    }
}
