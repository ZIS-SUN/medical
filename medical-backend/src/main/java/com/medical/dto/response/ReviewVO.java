package com.medical.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评价响应对象
 *
 * @author medical-system
 */
@Data
@Schema(description = "评价响应对象")
public class ReviewVO {

    @Schema(description = "评价ID")
    private Long id;

    @Schema(description = "患者ID")
    private Long userId;

    @Schema(description = "患者姓名")
    private String userName;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "医生姓名")
    private String doctorName;

    @Schema(description = "预约ID")
    private Long appointmentId;

    @Schema(description = "预约单号")
    private String appointmentNo;

    @Schema(description = "评分")
    private Integer rating;

    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "状态（0-隐藏，1-显示）")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
