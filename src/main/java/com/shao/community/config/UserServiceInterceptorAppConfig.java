package com.shao.community.config;

import com.shao.community.controller.UserServiceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author: shao
 * Date: 2020-09-20
 * Time: 15:38
 */
@Configuration
public class UserServiceInterceptorAppConfig implements WebMvcConfigurer {
    @Autowired
    UserServiceInterceptor userServiceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userServiceInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
