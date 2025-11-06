package com.medical.controller;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.dto.request.ConsultationReplyRequest;
import com.medical.dto.request.ConsultationRequest;
import com.medical.dto.response.ConsultationVO;
import com.medical.service.ConsultationService;
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
 * 问诊控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/consultations")
@Tag(name = "问诊接口", description = "在线问诊相关接口")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping
    @Operation(summary = "提交问诊")
    public Result<Map<String, String>> createConsultation(
            @Validated @RequestBody ConsultationRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        String consultationNo = consultationService.createConsultation(userId, request);

        Map<String, String> data = new HashMap<>();
        data.put("consultationNo", consultationNo);

        return Result.success(data);
    }

    @GetMapping("/my")
    @Operation(summary = "查询我的问诊列表")
    public Result<PageResult<ConsultationVO>> getMyConsultations(
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        PageResult<ConsultationVO> result = consultationService.getMyConsultations(
                userId, status, page, pageSize
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询问诊详情")
    public Result<ConsultationVO> getConsultationDetail(
            @Parameter(description = "问诊ID") @PathVariable Long id,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        ConsultationVO consultation = consultationService.getConsultationDetail(id, userId);
        return Result.success(consultation);
    }

    @PostMapping("/{id}/reply")
    @Operation(summary = "医生回复问诊")
    public Result<Void> replyConsultation(
            @Parameter(description = "问诊ID") @PathVariable Long id,
            @Validated @RequestBody ConsultationReplyRequest request,
            HttpServletRequest httpRequest
    ) {
        Long doctorId = (Long) httpRequest.getAttribute("doctorId");
        consultationService.replyConsultation(id, doctorId, request);
        return Result.success();
    }

    @GetMapping("/doctor/my")
    @Operation(summary = "查询医生的问诊列表")
    public Result<PageResult<ConsultationVO>> getDoctorConsultations(
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest
    ) {
        Long doctorId = (Long) httpRequest.getAttribute("doctorId");
        PageResult<ConsultationVO> result = consultationService.getDoctorConsultations(
                doctorId, status, page, pageSize
        );
        return Result.success(result);
    }
}
