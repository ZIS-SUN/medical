package com.medical.controller;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.dto.request.AppointmentRequest;
import com.medical.dto.response.AppointmentVO;
import com.medical.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 预约控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/appointments")
@Tag(name = "预约接口", description = "预约挂号相关接口")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "提交预约")
    public Result<Map<String, String>> createAppointment(
            @Validated @RequestBody AppointmentRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        String appointmentNo = appointmentService.createAppointment(userId, request);

        Map<String, String> data = new HashMap<>();
        data.put("appointmentNo", appointmentNo);

        return Result.success(data);
    }

    @GetMapping("/my")
    @Operation(summary = "查询我的预约")
    public Result<PageResult<AppointmentVO>> getMyAppointments(
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        PageResult<AppointmentVO> result = appointmentService.getMyAppointments(
                userId, status, page, pageSize
        );
        return Result.success(result);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消预约")
    public Result<Void> cancelAppointment(
            @Parameter(description = "预约ID") @PathVariable Long id,
            @RequestParam String cancelReason,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        appointmentService.cancelAppointment(id, userId, cancelReason);
        return Result.success();
    }
}
