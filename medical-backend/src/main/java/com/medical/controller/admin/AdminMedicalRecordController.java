package com.medical.controller.admin;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.dto.response.MedicalRecordVO;
import com.medical.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端病历控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/records")
@Tag(name = "管理端-病历管理", description = "管理端病历管理接口")
public class AdminMedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    @Operation(summary = "查询所有病历列表")
    public Result<PageResult<MedicalRecordVO>> getAllRecords(
            @Parameter(description = "关键词搜索") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<MedicalRecordVO> result = medicalRecordService.getAllRecords(
                keyword, page, pageSize
        );
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除病历记录")
    public Result<Void> deleteRecord(
            @Parameter(description = "病历ID") @PathVariable Long id
    ) {
        medicalRecordService.deleteRecord(id);
        return Result.success();
    }
}
