package com.medical.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密工具类
 *
 * @author medical-system
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    /**
     * 加密密码
     */
    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 生成admin123的加密密码
        String password = "admin123";
        String encoded = encode(password);
        System.out.println("密码：" + password);
        System.out.println("加密后：" + encoded);
        System.out.println("验证结果：" + matches(password, encoded));
    }
}
