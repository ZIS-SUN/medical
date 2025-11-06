package com.medical.controller.admin;

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
 * 管理端医生控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/doctors")
@Tag(name = "管理端-医生管理", description = "管理端医生管理接口")
public class AdminDoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    @Operation(summary = "获取医生列表")
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
}

