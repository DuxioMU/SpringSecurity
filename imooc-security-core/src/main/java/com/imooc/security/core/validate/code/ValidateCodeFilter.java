package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import sun.plugin2.os.windows.SECURITY_ATTRIBUTES;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean{

    /**
     * 验证码校验失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 系统配置信息
     */
    @Autowired
    private SecurityProperties securityProperties;
    /**
     * 系统中的校验码处理器
     */
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHandler;

    /**
     * 存放所有需要校验验证码的url
     */
    private Map<String,ValidateCodeType> urlMap = new HashMap<>();

    /**
     * 验证请求url与配置的url是否匹配的的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();


    /**
     * 初始化要拦截的url配置信息
     * @throws ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
//        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getImage().getUrl(), ",");
//        for (String configUrl :configUrls){
//            urls.add(configUrl);
//        }
//        urls.add("/authentication/form");
        urlMap.put(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM,ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(),ValidateCodeType.IMAGE);

        urlMap.put(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(),ValidateCodeType.SMS);
    }
    //工具
    protected  void addUrlToMap(String urlString,ValidateCodeType type){
        if (StringUtils.isNotBlank(urlString)){
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString,",");
            for (String url : urls) {
                urlMap.put(url, type);
            }
        }
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

//        if(StringUtils.equals("/authentication/form", httpServletRequest.getRequestURI())
//                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(),"post")){
//
//        boolean action = false;
//        for (String url : urls){
//            if(pathMatcher.match(url,httpServletRequest.getRequestURI())){
//                action = true;
//            }
//        }
        ValidateCodeType type = getValidateCode(httpServletRequest);
        if(type != null){
            try{
                validateCodeProcessorHandler.findValidateCodeProcessor(type);
            }catch(ValidateCodeException e){
                logger.info("验证码校验失败："+e.getMessage());
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null；
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCode(HttpServletRequest request){
        ValidateCodeType result = null;
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                }
            }
        }
        return result;
    }

//    private void validate(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
//
//        //ValidateCodeType codeType = getValidateCodeType(request);
//        ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(servletWebRequest ,ValidateCodeProcessor.SESSION_KEY_PREFIX+"IMAGE" );
//
//       // C codeInSession = (C) validateCodeRepository.get(request, codeType);
//
//        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),"imageCode");
//
////        try {
////            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
////                    codeType.getParamNameOnValidate());
////        } catch (ServletRequestBindingException e) {
////            throw new ValidateCodeException("获取验证码的值失败");
////        }
//
//        if (StringUtils.isBlank(codeInRequest)) {
//            throw new ValidateCodeException( "验证码的值不能为空");
//        }
//
//        if (codeInSession == null) {
//            throw new ValidateCodeException( "验证码不存在");
//        }
//
//        if (codeInSession.isExpireTime()){
//            sessionStrategy.removeAttribute(servletWebRequest,ValidateCodeProcessor.SESSION_KEY_PREFIX+"IMAGE");
//            throw new ValidateCodeException( "验证码已过期");
//        }
//
//        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
//            throw new ValidateCodeException( "验证码不匹配");
//        }
//
//        sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeProcessor.SESSION_KEY_PREFIX+"IMAGE");
//
//    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}
