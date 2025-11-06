package com.medical.controller;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.dto.response.DoctorVO;
import com.medical.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 医生控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/doctors")
@Tag(name = "医生接口", description = "医生查询相关接口")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    @Operation(summary = "分页查询医生列表")
    public Result<PageResult<DoctorVO>> getDoctorList(
            @Parameter(description = "科室ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "职称") @RequestParam(required = false) String title,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<DoctorVO> result = doctorService.getDoctorList(
                departmentId, title, keyword, page, pageSize
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取医生详情")
    public Result<DoctorVO> getDoctorDetail(
            @Parameter(description = "医生ID") @PathVariable Long id
    ) {
        DoctorVO doctor = doctorService.getDoctorDetail(id);
        return Result.success(doctor);
    }

    @GetMapping("/hot")
    @Operation(summary = "获取热门医生列表")
    public Result<java.util.List<DoctorVO>> getHotDoctors() {
        java.util.List<DoctorVO> doctors = doctorService.getHotDoctors();
        return Result.success(doctors);
    }
}
