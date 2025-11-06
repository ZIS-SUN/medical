package com.medical.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 预约请求
 *
 * @author medical-system
 */
@Data
@Schema(description = "预约请求")
public class AppointmentRequest {

    @NotNull(message = "医生ID不能为空")
    @Schema(description = "医生ID", required = true)
    private Long doctorId;

    @NotNull(message = "预约日期不能为空")
    @Schema(description = "预约日期", required = true, example = "2025-11-10")
    private LocalDate appointmentDate;

    @NotBlank(message = "时间段不能为空")
    @Schema(description = "时间段：上午/下午", required = true)
    private String timeSlot;

    @Schema(description = "备注")
    private String remark;
}
