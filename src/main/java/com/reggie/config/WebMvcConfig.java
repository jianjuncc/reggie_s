package com.reggie.config;

import com.reggie.interceptor.MainInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * web配置类
 * @author shu
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new MainInterceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns(
                //登录路径
                "/employee/login",
                "/employee/logout",
                //html静态资源
                "/**/*.html",
                "/backend/images/**",
                "/backend/plugins/**",
                "/backend/styles/**",
                //js静态资源
                "/**/*.js",
                //css静态资源
                "/**/*.css"

        );
    }
    /***
     * 配置资源映射
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("静态资源开始加载");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
}
