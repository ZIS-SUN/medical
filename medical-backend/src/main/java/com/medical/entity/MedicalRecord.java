package com.medical.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 病历实体
 *
 * @author medical-system
 */
@Data
@TableName("medical_record")
@Schema(description = "病历实体")
public class MedicalRecord {

    @TableId(type = IdType.AUTO)
    @Schema(description = "病历ID")
    private Long id;

    @Schema(description = "病历号")
    private String recordNo;

    @Schema(description = "预约ID")
    private Long appointmentId;

    @Schema(description = "患者ID")
    private Long userId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "主诉")
    private String chiefComplaint;

    @Schema(description = "现病史")
    private String presentIllness;

    @Schema(description = "诊断结果")
    private String diagnosis;

    @Schema(description = "处方内容")
    private String prescription;

    @Schema(description = "医嘱建议")
    private String advice;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
