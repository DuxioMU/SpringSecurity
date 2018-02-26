package com.imooc.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Aspect
//@Component
public class TimeAspect {

    //声明 一个切入点11.1.2
    @Around("execution(* com.imooc.web.controller.UserController.*(..))")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("time aspect start ");

        Object[] args = pjp.getArgs();
        for(Object arg:args){
            System.out.println("method arg ："+arg);
        }
        long start = new Date().getTime();

        Object object = pjp.proceed();
        System.out.println("time aspect 耗时:"+(new Date().getTime() - start));
        System.out.println("time aspect end");

        return object;
    }

}
