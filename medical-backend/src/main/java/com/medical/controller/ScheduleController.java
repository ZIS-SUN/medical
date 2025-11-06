package com.medical.controller;

import com.medical.common.Result;
import com.medical.entity.Schedule;
import com.medical.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/schedules")
@Tag(name = "排班接口", description = "医生排班查询接口")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "获取医生排班列表")
    public Result<List<Schedule>> getDoctorSchedules(@PathVariable Long doctorId) {
        List<Schedule> schedules = scheduleService.getDoctorSchedules(doctorId);
        return Result.success(schedules);
    }

    @GetMapping("/doctor/{doctorId}/date/{date}")
    @Operation(summary = "获取医生指定日期的排班")
    public Result<List<Schedule>> getDoctorSchedulesByDate(
            @PathVariable Long doctorId,
            @PathVariable String date) {
        LocalDate workDate = LocalDate.parse(date);
        List<Schedule> schedules = scheduleService.getDoctorSchedulesByDate(doctorId, workDate);
        return Result.success(schedules);
    }
}
