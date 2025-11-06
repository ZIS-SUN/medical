package com.medical.dto.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 问诊导出DTO
 *
 * @author medical-system
 */
@Data
public class ConsultationExportDTO {

    @ExcelProperty("问诊单号")
    @ColumnWidth(20)
    private String consultationNo;

    @ExcelProperty("患者姓名")
    @ColumnWidth(15)
    private String userName;

    @ExcelProperty("医生姓名")
    @ColumnWidth(15)
    private String doctorName;

    @ExcelProperty("科室名称")
    @ColumnWidth(15)
    private String departmentName;

    @ExcelProperty("患者问题")
    @ColumnWidth(40)
    private String question;

    @ExcelProperty("医生回复")
    @ColumnWidth(40)
    private String answer;

    @ExcelProperty("状态")
    @ColumnWidth(12)
    private String status;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private String createTime;
}
