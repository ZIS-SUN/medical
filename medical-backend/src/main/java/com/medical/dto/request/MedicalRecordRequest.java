package com.medical.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 病历请求
 *
 * @author medical-system
 */
@Data
@Schema(description = "病历请求")
public class MedicalRecordRequest {

    @NotNull(message = "预约ID不能为空")
    @Schema(description = "预约ID")
    private Long appointmentId;

    @NotNull(message = "患者ID不能为空")
    @Schema(description = "患者ID")
    private Long userId;

    @NotBlank(message = "主诉不能为空")
    @Schema(description = "主诉")
    private String chiefComplaint;

    @Schema(description = "现病史")
    private String presentIllness;

    @NotBlank(message = "诊断结果不能为空")
    @Schema(description = "诊断结果")
    private String diagnosis;

    @Schema(description = "处方内容")
    private String prescription;

    @Schema(description = "医嘱建议")
    private String advice;
}
