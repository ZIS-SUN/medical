package com.medical.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约实体
 *
 * @author medical-system
 */
@Data
@TableName("appointment")
@Schema(description = "预约实体")
public class Appointment {

    @TableId(type = IdType.AUTO)
    @Schema(description = "预约ID")
    private Long id;

    @Schema(description = "预约单号")
    private String appointmentNo;

    @Schema(description = "患者ID")
    private Long userId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "科室ID")
    private Long departmentId;

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

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
