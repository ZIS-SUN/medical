package com.medical.controller.admin;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.entity.User;
import com.medical.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端用户控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "管理端-用户管理", description = "管理端用户管理接口")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "获取用户列表")
    public Result<PageResult<User>> getUserList(
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<User> result = userService.getUserList(keyword, status, page, pageSize);
        return Result.success(result);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "切换用户状态")
    public Result<Void> toggleUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status
    ) {
        userService.toggleUserStatus(id, status);
        return Result.success();
    }
}

