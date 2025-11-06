package com.medical;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 医疗问诊管理系统启动类
 *
 * @author medical-system
 * @since 2025-11-05
 */
@SpringBootApplication
@MapperScan("com.medical.mapper")
public class MedicalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalApplication.class, args);
        System.out.println("\n====================================");
        System.out.println("医疗问诊管理系统启动成功！");
        System.out.println("接口文档地址：http://localhost:8080/doc.html");
        System.out.println("====================================\n");
    }
}
