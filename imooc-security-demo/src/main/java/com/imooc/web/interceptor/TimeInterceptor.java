package com.imooc.web.interceptor;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

//@Component
public class TimeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("preHandler");

        System.out.println(((HandlerMethod)o).getBean().getClass().getName());
        System.out.println(((HandlerMethod)o).getMethod().getName());
        httpServletRequest.setAttribute("startTime",new Date().getTime());
        //true继续，false拦截
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
        Long start = (Long)httpServletRequest.getAttribute("startTime");
        System.out.println("time interceptor 耗时："+ (new Date().getTime() - start));
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("afterCompletiion");
        Long start = (Long)httpServletRequest.getAttribute("startTime");
        System.out.println("time interceptor 耗时："+ (new Date().getTime() - start));
        //异常为空因为异常被异常处理器处理了。
        //拦截器会拦截所有的Controller的调用包括spring提供的BasicErrorController
        System.out.println("ex is: "+ e);
    }
}
