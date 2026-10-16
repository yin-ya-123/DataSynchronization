package com.yin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings (CorsRegistry registry) {
        //将一个新的映射添加到CORS注册表中，该映射将允许来自任何源（/**）的请求。
        registry.addMapping("/**")
                //设置了允许的Origin模式为ALL，这意味着来自任何域名的请求都将被允许。
                .allowedOriginPatterns(CorsConfiguration.ALL)
                //设置了允许的所有HTTP方法（GET、POST等）
                .allowedMethods(CorsConfiguration.ALL)
                //设置了允许的所有HTTP头。
                .allowedHeaders(CorsConfiguration.ALL)
                //设置了是否允许跨域请求携带凭证（withCredentials），这里设置为true，表示允许携带凭证。
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        registry.addInterceptor(new RequestTimeInterceptor());
    }

    // 配置路由转发：将所有非API请求转发到index.html
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 只转发以/vue开头的路径，且排除静态资源（含.的路径）
        // 新版路径匹配策略下的正确写法
        registry.addViewController("/vue/**")
                .setViewName("forward:/index.html");
    }
}

