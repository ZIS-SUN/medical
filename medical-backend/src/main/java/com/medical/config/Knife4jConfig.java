package com.medical.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j配置
 *
 * @author medical-system
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("医疗问诊管理系统API文档")
                        .description("基于Spring Boot的医疗问诊管理系统后端接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("medical-system")
                                .email("medical@example.com")));
    }
}
