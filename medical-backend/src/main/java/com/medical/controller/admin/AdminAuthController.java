package com.medical.controller.admin;

import com.medical.common.Result;
import com.medical.dto.request.LoginRequest;
import com.medical.dto.response.LoginResponse;
import com.medical.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端认证控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/auth")
@Tag(name = "管理端认证接口", description = "管理员登录相关接口")
public class AdminAuthController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        LoginResponse response = adminService.login(request);
        return Result.success(response);
    }
}
