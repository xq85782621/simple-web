package com.meizhi.config;

import com.meizhi.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 拦截器配置类
 */
@Configuration
public class InterceptorConfig  extends WebMvcConfigurationSupport {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**")  // 拦截所有请求
        .excludePathPatterns("/login","logout");  // 登录相关接口不拦截
    }

    @Bean
    public JwtInterceptor authenticationInterceptor() {
        return new JwtInterceptor();
    }

}
