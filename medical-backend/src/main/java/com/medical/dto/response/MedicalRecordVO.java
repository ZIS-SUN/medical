package com.medical.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 病历响应对象
 *
 * @author medical-system
 */
@Data
@Schema(description = "病历响应对象")
public class MedicalRecordVO {

    @Schema(description = "病历ID")
    private Long id;

    @Schema(description = "病历号")
    private String recordNo;

    @Schema(description = "预约ID")
    private Long appointmentId;

    @Schema(description = "预约单号")
    private String appointmentNo;

    @Schema(description = "患者ID")
    private Long userId;

    @Schema(description = "患者姓名")
    private String userName;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "医生姓名")
    private String doctorName;

    @Schema(description = "科室名称")
    private String departmentName;

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
