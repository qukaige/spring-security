package com.imooc.web.config;

import com.imooc.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class TimeInterceptorConfig extends WebMvcConfigurerAdapter {
    // 自定义的Interceptor
    @Autowired
    private TimeInterceptor timeInterceptor;

    // 注入spring
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);
    }

    /**
     * 在异步的情况下,就需要使用这个方法
     * @param configurer
     */
    /*@Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.registerCallableInterceptors(timeInterceptor);
    }*/
}
