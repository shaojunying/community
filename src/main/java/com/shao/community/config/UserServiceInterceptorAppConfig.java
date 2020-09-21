package com.shao.community.config;

import com.shao.community.controller.UserServiceInterceptor;
import com.shao.community.interceptor.LoginRequiredInterceptor;
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

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userServiceInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        // 拦截一些需要登录却没有登陆的请求
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
