package com.medical.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 医生实体
 *
 * @author medical-system
 */
@Data
@TableName("doctor")
@Schema(description = "医生实体")
public class Doctor {

    @TableId(type = IdType.AUTO)
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

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "医生简介")
    private String profile;

    @Schema(description = "状态：0-离职 1-在职")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
