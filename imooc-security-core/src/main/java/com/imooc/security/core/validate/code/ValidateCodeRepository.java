package com.imooc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeRepository {

    void save(ServletWebRequest request, ValidateCode code,ValidateCodeType validateCodeType);
}
