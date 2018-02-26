package com.imooc.code;

import com.imooc.security.core.authentication.validate.code.ImageCode;
import com.imooc.security.core.authentication.validate.code.ValidateCodeGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 自定义demo的图片生成器
 * 高级编程 以增量的方式适应变化
 */
//@Component("imageCodeGenerator")//优先于配置文件配置的bean
public class DemoImageCodeGenerator implements ValidateCodeGenerator{

    @Override
    public ImageCode generate(ServletWebRequest request) {
        System.out.println("更高级的图形验证码生成代码");
        return null;
    }
}
