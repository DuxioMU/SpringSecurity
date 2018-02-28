package com.imooc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器，封装不同的校验码的处理逻辑
 */
public interface ValidateCodeProcessor {

    /**
     *验证码放入session时的前缀
     * @param request
     * @throws Exception
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE";

    void create(ServletWebRequest request) throws Exception;

    void validate(ServletWebRequest request);
}
