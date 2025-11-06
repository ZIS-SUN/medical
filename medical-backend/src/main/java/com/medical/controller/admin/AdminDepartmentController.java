package com.medical.controller.admin;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.entity.Department;
import com.medical.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端科室控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/departments")
@Tag(name = "管理端-科室管理", description = "管理端科室管理接口")
public class AdminDepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "获取科室列表")
    public Result<PageResult<Department>> getDepartmentList(
            @Parameter(description = "科室类型") @RequestParam(required = false) String type,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<Department> result = departmentService.getDepartmentList(type, page, pageSize);
        return Result.success(result);
    }
}

