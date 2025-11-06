package com.medical.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评价请求
 *
 * @author medical-system
 */
@Data
@Schema(description = "评价请求")
public class ReviewRequest {

    @NotNull(message = "医生ID不能为空")
    @Schema(description = "医生ID")
    private Long doctorId;

    @NotNull(message = "预约ID不能为空")
    @Schema(description = "预约ID")
    private Long appointmentId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1分")
    @Max(value = 5, message = "评分最高为5分")
    @Schema(description = "评分（1-5分）")
    private Integer rating;

    @NotBlank(message = "评价内容不能为空")
    @Schema(description = "评价内容")
    private String content;
}
