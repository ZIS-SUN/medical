package com.medical.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约响应VO
 *
 * @author medical-system
 */
@Data
@Schema(description = "预约响应")
public class AppointmentVO {

    @Schema(description = "预约ID")
    private Long id;

    @Schema(description = "预约单号")
    private String appointmentNo;

    @Schema(description = "患者ID")
    private Long userId;

    @Schema(description = "患者姓名")
    private String userName;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "医生姓名")
    private String doctorName;

    @Schema(description = "科室名称")
    private String departmentName;

    @Schema(description = "预约日期")
    private LocalDate appointmentDate;

    @Schema(description = "时间段")
    private String timeSlot;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "取消原因")
    private String cancelReason;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
