package com.imooc.security.browser;


import com.imooc.security.core.authentication.FormAuthenticationConfig;
import com.imooc.security.core.authentication.sms.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private FormAuthenticationConfig formAuthenticationConfig;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //自动创建表
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

//    @Bean
//    public  ObjectMapper objectMapper(){
//        return new ObjectMapper();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
//        validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
//        validateCodeFilter.setSecurityProperties(securityProperties);
//        validateCodeFilter.afterPropertiesSet();
//
//        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
//        smsCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
//        smsCodeFilter.setSecurityProperties(securityProperties);
//        smsCodeFilter.afterPropertiesSet();

        formAuthenticationConfig.configure(http);

        http.apply(validateCodeSecurityConfig)
            .and()
            .apply(smsCodeAuthenticationSecurityConfig)
            .and()
            .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSecods())
                .userDetailsService(myUserDetailsService)
            .and()
            .authorizeRequests()
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN__PROCESSING_URL,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*")
                        .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .csrf().disable()//跨站请求伪造
            ;
    }

}
