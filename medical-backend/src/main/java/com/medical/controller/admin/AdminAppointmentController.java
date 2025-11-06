package com.medical.controller.admin;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.dto.response.AppointmentVO;
import com.medical.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端预约控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/appointments")
@Tag(name = "管理端-预约管理", description = "管理端预约管理接口")
public class AdminAppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    @Operation(summary = "获取预约列表")
    public Result<PageResult<AppointmentVO>> getAppointmentList(
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        // 查询所有预约（不限用户）
        PageResult<AppointmentVO> result = appointmentService.getAppointmentList(status, page, pageSize);
        return Result.success(result);
    }
}

