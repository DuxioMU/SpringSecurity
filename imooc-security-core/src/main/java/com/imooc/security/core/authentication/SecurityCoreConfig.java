package com.imooc.security.core.authentication;

import com.imooc.security.core.authentication.properties.BrowserProperties;
import com.imooc.security.core.authentication.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;

/**
 * 配置读取文件信息的对象
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {


    private BrowserProperties browser = new BrowserProperties();

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
    public BrowserProperties getBrowser(){
        return browser;
    }
}
