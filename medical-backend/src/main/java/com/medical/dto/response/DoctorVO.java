package com.medical.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 医生响应VO
 *
 * @author medical-system
 */
@Data
@Schema(description = "医生响应")
public class DoctorVO {

    @Schema(description = "医生ID")
    private Long id;

    @Schema(description = "医生姓名")
    private String name;

    @Schema(description = "性别：1-男 2-女")
    private Integer gender;

    @Schema(description = "职称")
    private String title;

    @Schema(description = "科室ID")
    private Long departmentId;

    @Schema(description = "科室名称")
    private String departmentName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "医生简介")
    private String profile;

    @Schema(description = "平均评分")
    private Double avgRating;

    @Schema(description = "评价数量")
    private Long reviewCount;
}
