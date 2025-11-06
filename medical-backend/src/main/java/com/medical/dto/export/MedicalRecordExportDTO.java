package com.medical.dto.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 病历导出DTO
 *
 * @author medical-system
 */
@Data
public class MedicalRecordExportDTO {

    @ExcelProperty("病历号")
    @ColumnWidth(20)
    private String recordNo;

    @ExcelProperty("患者姓名")
    @ColumnWidth(15)
    private String userName;

    @ExcelProperty("医生姓名")
    @ColumnWidth(15)
    private String doctorName;

    @ExcelProperty("科室名称")
    @ColumnWidth(15)
    private String departmentName;

    @ExcelProperty("主诉")
    @ColumnWidth(30)
    private String chiefComplaint;

    @ExcelProperty("诊断结果")
    @ColumnWidth(30)
    private String diagnosis;

    @ExcelProperty("处方内容")
    @ColumnWidth(30)
    private String prescription;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private String createTime;
}
