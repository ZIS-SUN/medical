package com.medical.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 问诊实体
 *
 * @author medical-system
 */
@Data
@TableName("consultation")
@Schema(description = "问诊实体")
public class Consultation {

    @TableId(type = IdType.AUTO)
    @Schema(description = "问诊ID")
    private Long id;

    @Schema(description = "问诊单号")
    private String consultationNo;

    @Schema(description = "患者ID")
    private Long userId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "患者问题")
    private String question;

    @Schema(description = "问题图片")
    private String questionImages;

    @Schema(description = "医生回复")
    private String answer;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "回复时间")
    private LocalDateTime replyTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
