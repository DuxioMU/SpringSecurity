package com.imooc.web.controller;

import com.imooc.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于处理控制器所抛出异常
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,Object> handleUserNotException(UserNotExistException ex){
        Map<String ,Object> result = new HashMap();
        result.put("id",ex.getId());
        result.put("message",ex.getMessage());
        return result;
    }
}
