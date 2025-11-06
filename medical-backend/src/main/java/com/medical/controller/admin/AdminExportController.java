package com.medical.controller.admin;

import com.medical.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理端导出控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/export")
@Tag(name = "管理端-数据导出", description = "管理端数据导出接口")
public class AdminExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/appointments")
    @Operation(summary = "导出预约数据")
    public void exportAppointments(
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            HttpServletResponse response
    ) throws IOException {
        exportService.exportAppointments(response, status);
    }

    @GetMapping("/consultations")
    @Operation(summary = "导出问诊数据")
    public void exportConsultations(
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            HttpServletResponse response
    ) throws IOException {
        exportService.exportConsultations(response, status);
    }

    @GetMapping("/records")
    @Operation(summary = "导出病历数据")
    public void exportMedicalRecords(HttpServletResponse response) throws IOException {
        exportService.exportMedicalRecords(response);
    }

    @GetMapping("/reviews")
    @Operation(summary = "导出评价数据")
    public void exportReviews(
            @Parameter(description = "状态筛选") @RequestParam(required = false) Integer status,
            HttpServletResponse response
    ) throws IOException {
        exportService.exportReviews(response, status);
    }
}
