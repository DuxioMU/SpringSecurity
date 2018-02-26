package com.imooc.web.config;

import com.imooc.web.filter.TimeFilter;
import com.imooc.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置拦截器需要继承WebMvcConfigurerAdapter
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

//    @Autowired
//    private TimeInterceptor timeInterceptor;


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(timeInterceptor);
//    }

//    @Bean
//    public FilterRegistrationBean timeFilter(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        //与直接用@Component不同的是，可以指定不同的Url上使用
//        TimeFilter timeFilter = new TimeFilter();
//        registrationBean.setFilter(timeFilter);
//
//        List<String> urls = new ArrayList<>();
//        urls.add("/*");
//        registrationBean.setUrlPatterns(urls);
//        return registrationBean;
//    }


}
