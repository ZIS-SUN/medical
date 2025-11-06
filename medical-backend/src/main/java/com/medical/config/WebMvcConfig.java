package com.medical.config;

import com.medical.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 *
 * @author medical-system
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                // 需要认证的路径
                .addPathPatterns("/api/**", "/admin/**")
                // 排除的路径
                .excludePathPatterns(
                        "/api/auth/**",          // 登录注册
                        "/api/departments/**",   // 科室列表
                        "/api/doctors/**",       // 医生列表（公开查询）
                        "/api/schedules/**",     // 排班查询（公开查询）
                        "/api/announcements/**", // 公告列表
                        "/api/reviews/doctor/**", // 医生评价查询（公开查询）
                        "/api/admin/auth/**",    // 管理员登录（修复路径）
                        "/admin/auth/**",        // 管理员登录（备用路径）
                        "/swagger-ui/**",        // 接口文档
                        "/doc.html",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/favicon.ico"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源映射（文件上传）
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
