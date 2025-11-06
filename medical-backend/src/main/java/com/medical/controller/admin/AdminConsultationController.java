package com.medical.controller.admin;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.dto.response.ConsultationVO;
import com.medical.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端问诊控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/consultations")
@Tag(name = "管理端-问诊管理", description = "管理端问诊管理接口")
public class AdminConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @GetMapping
    @Operation(summary = "查询所有问诊列表")
    public Result<PageResult<ConsultationVO>> getAllConsultations(
            @Parameter(description = "关键词搜索") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<ConsultationVO> result = consultationService.getAllConsultations(
                keyword, status, page, pageSize
        );
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除问诊记录")
    public Result<Void> deleteConsultation(
            @Parameter(description = "问诊ID") @PathVariable Long id
    ) {
        consultationService.deleteConsultation(id);
        return Result.success();
    }
}
