package com.imooc.security.core.authentication.properties;

import com.imooc.security.core.authentication.properties.BrowserProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 读取配置文件内的配置值到对象里面
 */
@ConfigurationProperties(prefix = "imooc.security")
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }


}
