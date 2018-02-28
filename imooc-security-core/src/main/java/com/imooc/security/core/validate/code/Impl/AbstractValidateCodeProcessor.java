package com.imooc.security.core.validate.code.Impl;

import com.imooc.security.core.validate.code.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     * @param request
     * @throws Exception
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator}接口
     * 定向搜索某个接口实现
     * @param request
     * @throws Exception
     */
    @Autowired
    private Map<String,ValidateCodeGenerator> validateCodeGenerators;


    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = generate(request);
        save(request,validateCode);
        send(request,validateCode);
    }

//    public abstract void validate(ServletWebRequest sevletWebRequest);
//


    private C generate(ServletWebRequest request){
        String type = getValidateCodeType(request).toString().toLowerCase();
        //String generatorName = type + "CodeGenerator";
        String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
        if (validateCodeGenerator == null){
            throw new ValidateCodeException("验证码生成器" + generatorName+"不存在");
        }
        return (C)validateCodeGenerator.generate(request);
    }

    private void save(ServletWebRequest request ,C validateCode){
        sessionStrategy.setAttribute(request ,SESSION_KEY_PREFIX+getValidateCodeType(request),validateCode);
    }


    /**
     * 发送校验码，由子类实现
     * @param request
     * @param validateCode
     * @throws Exception
     */
    protected abstract void send(ServletWebRequest request,C validateCode)throws Exception;

    /**
     * 根据请求的url获取校验码类型
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request){
       // return StringUtils.substringAfter(request.getRequest().getRequestURI(),"/code/");
        String type = StringUtils.substringBefore(getClass().getSimpleName(),ValidateCodeProcessor.class.getSimpleName());
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    public void validate(ServletWebRequest request){

        ValidateCodeType codeType = getValidateCodeType(request);

        C codeInSession = (C)sessionStrategy.getAttribute(request, SESSION_KEY_PREFIX + getValidateCodeType(request));

        String codeInRequest = null;
        try {
            //codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "smsCode");
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException(codeType+"获取验证码的值失败");
        }

//        try {
//            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
//                    codeType.getParamNameOnValidate());
//        } catch (ServletRequestBindingException e) {
//            throw new ValidateCodeException("获取验证码的值失败");
//        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(codeType+"验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(codeType+"验证码不存在");
        }

        if (codeInSession.isExpireTime()) {
            sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX + codeType);
            throw new ValidateCodeException(codeType+"验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(codeType+"验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX + codeType);

    }
}
