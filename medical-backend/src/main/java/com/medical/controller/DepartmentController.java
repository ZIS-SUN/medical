package com.medical.controller;

import com.medical.common.Result;
import com.medical.dto.response.DepartmentVO;
import com.medical.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 科室控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/departments")
@Tag(name = "科室接口", description = "科室查询相关接口")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "获取科室树形列表")
    public Result<List<DepartmentVO>> getDepartmentTree() {
        List<DepartmentVO> tree = departmentService.getDepartmentTree();
        return Result.success(tree);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有科室（平铺列表）")
    public Result<List<DepartmentVO>> getAllDepartments() {
        List<DepartmentVO> list = departmentService.getAllDepartments();
        return Result.success(list);
    }
}
