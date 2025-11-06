package com.medical.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 问诊请求
 *
 * @author medical-system
 */
@Data
@Schema(description = "问诊请求")
public class ConsultationRequest {

    @NotNull(message = "医生ID不能为空")
    @Schema(description = "医生ID")
    private Long doctorId;

    @NotBlank(message = "问题描述不能为空")
    @Schema(description = "患者问题")
    private String question;

    @Schema(description = "问题图片（多个图片用逗号分隔）")
    private String questionImages;
}
