package com.medical.controller;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.dto.request.MedicalRecordRequest;
import com.medical.dto.response.MedicalRecordVO;
import com.medical.service.MedicalRecordService;
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
 * 病历控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/records")
@Tag(name = "病历接口", description = "电子病历相关接口")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping
    @Operation(summary = "创建病历")
    public Result<Map<String, String>> createMedicalRecord(
            @Validated @RequestBody MedicalRecordRequest request,
            HttpServletRequest httpRequest
    ) {
        Long doctorId = (Long) httpRequest.getAttribute("doctorId");
        String recordNo = medicalRecordService.createMedicalRecord(doctorId, request);

        Map<String, String> data = new HashMap<>();
        data.put("recordNo", recordNo);

        return Result.success(data);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新病历")
    public Result<Void> updateMedicalRecord(
            @Parameter(description = "病历ID") @PathVariable Long id,
            @Validated @RequestBody MedicalRecordRequest request,
            HttpServletRequest httpRequest
    ) {
        Long doctorId = (Long) httpRequest.getAttribute("doctorId");
        medicalRecordService.updateMedicalRecord(id, doctorId, request);
        return Result.success();
    }

    @GetMapping("/my")
    @Operation(summary = "查询我的病历列表")
    public Result<PageResult<MedicalRecordVO>> getMyRecords(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        PageResult<MedicalRecordVO> result = medicalRecordService.getPatientRecords(
                userId, page, pageSize
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询病历详情")
    public Result<MedicalRecordVO> getRecordDetail(
            @Parameter(description = "病历ID") @PathVariable Long id,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        MedicalRecordVO record = medicalRecordService.getRecordDetail(id, userId);
        return Result.success(record);
    }

    @GetMapping("/doctor/my")
    @Operation(summary = "查询医生的病历列表")
    public Result<PageResult<MedicalRecordVO>> getDoctorRecords(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest
    ) {
        Long doctorId = (Long) httpRequest.getAttribute("doctorId");
        PageResult<MedicalRecordVO> result = medicalRecordService.getDoctorRecords(
                doctorId, page, pageSize
        );
        return Result.success(result);
    }
}
