package com.medical.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 医生回复请求
 *
 * @author medical-system
 */
@Data
@Schema(description = "医生回复请求")
public class ConsultationReplyRequest {

    @NotBlank(message = "回复内容不能为空")
    @Schema(description = "回复内容")
    private String answer;
}
