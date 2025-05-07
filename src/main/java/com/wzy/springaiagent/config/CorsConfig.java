package com.wzy.springaiagent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有接口
            .allowedOrigins("http://localhost:63342") // 允许的前端地址
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的方法
            .allowedHeaders("*") // 允许所有头
            .allowCredentials(false) // 允许携带 Cookie
            .maxAge(3600); // 预检请求缓存时间
    }
}
