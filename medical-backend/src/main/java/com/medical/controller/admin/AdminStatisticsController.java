package com.medical.controller.admin;

import com.medical.common.Result;
import com.medical.dto.response.StatisticsVO;
import com.medical.dto.response.TrendDataVO;
import com.medical.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理端统计控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/statistics")
@Tag(name = "管理端-统计分析", description = "管理端统计分析接口")
public class AdminStatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/overview")
    @Operation(summary = "获取统计概览数据")
    public Result<StatisticsVO> getStatistics() {
        StatisticsVO statistics = statisticsService.getStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/appointment-trend")
    @Operation(summary = "获取预约趋势数据")
    public Result<List<TrendDataVO>> getAppointmentTrend() {
        List<TrendDataVO> trendData = statisticsService.getAppointmentTrend();
        return Result.success(trendData);
    }

    @GetMapping("/consultation-trend")
    @Operation(summary = "获取问诊趋势数据")
    public Result<List<TrendDataVO>> getConsultationTrend() {
        List<TrendDataVO> trendData = statisticsService.getConsultationTrend();
        return Result.success(trendData);
    }

    @GetMapping("/appointment-status")
    @Operation(summary = "获取预约状态分布")
    public Result<List<TrendDataVO>> getAppointmentStatusDistribution() {
        List<TrendDataVO> distribution = statisticsService.getAppointmentStatusDistribution();
        return Result.success(distribution);
    }

    @GetMapping("/consultation-status")
    @Operation(summary = "获取问诊状态分布")
    public Result<List<TrendDataVO>> getConsultationStatusDistribution() {
        List<TrendDataVO> distribution = statisticsService.getConsultationStatusDistribution();
        return Result.success(distribution);
    }
}
