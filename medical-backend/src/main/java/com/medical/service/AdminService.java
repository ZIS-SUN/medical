package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical.common.BusinessException;
import com.medical.dto.request.LoginRequest;
import com.medical.dto.response.LoginResponse;
import com.medical.entity.Admin;
import com.medical.mapper.AdminMapper;
import com.medical.util.JwtUtil;
import com.medical.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 管理员服务
 *
 * @author medical-system
 */
@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 管理员登录
     */
    public LoginResponse login(LoginRequest request) {
        // 查询管理员
        Admin admin = adminMapper.selectOne(
                new LambdaQueryWrapper<Admin>()
                        .eq(Admin::getUsername, request.getUsername())
        );

        if (admin == null) {
            throw new BusinessException(10003, "用户名或密码错误");
        }

        // 验证密码
        if (!PasswordUtil.matches(request.getPassword(), admin.getPassword())) {
            throw new BusinessException(10003, "用户名或密码错误");
        }

        // 检查状态
        if (admin.getStatus() == 0) {
            throw new BusinessException(403, "账号已被禁用");
        }

        // 更新最后登录时间
        admin.setLastLoginTime(LocalDateTime.now());
        adminMapper.updateById(admin);

        // 生成Token（使用管理员Token生成方法）
        String token = jwtUtil.generateAdminToken(
                admin.getId(),
                admin.getUsername(),
                admin.getRole(),
                admin.getDoctorId()
        );

        // 构造响应
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                admin.getId(),
                admin.getUsername(),
                admin.getRealName(),
                null  // 管理员没有头像字段
        );

        return new LoginResponse(token, userInfo);
    }
}
