package com.medical.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统计数据响应对象
 *
 * @author medical-system
 */
@Data
@Schema(description = "统计数据响应对象")
public class StatisticsVO {

    @Schema(description = "用户总数")
    private Long totalUsers;

    @Schema(description = "医生总数")
    private Long totalDoctors;

    @Schema(description = "预约总数")
    private Long totalAppointments;

    @Schema(description = "问诊总数")
    private Long totalConsultations;

    @Schema(description = "病历总数")
    private Long totalRecords;

    @Schema(description = "评价总数")
    private Long totalReviews;

    @Schema(description = "今日预约数")
    private Long todayAppointments;

    @Schema(description = "今日问诊数")
    private Long todayConsultations;

    @Schema(description = "待确认预约数")
    private Long pendingAppointments;

    @Schema(description = "待回复问诊数")
    private Long pendingConsultations;
}
