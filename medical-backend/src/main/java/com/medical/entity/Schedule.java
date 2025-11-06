package com.medical.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 排班实体
 *
 * @author medical-system
 */
@Data
@TableName("schedule")
@Schema(description = "排班实体")
public class Schedule {

    @TableId(type = IdType.AUTO)
    @Schema(description = "排班ID")
    private Long id;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "排班日期")
    private LocalDate workDate;

    @Schema(description = "时间段")
    private String timeSlot;

    @Schema(description = "最大预约数")
    private Integer maxAppointments;

    @Schema(description = "已预约数")
    private Integer bookedCount;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
