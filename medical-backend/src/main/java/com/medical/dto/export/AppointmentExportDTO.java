package com.medical.dto.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 预约导出DTO
 *
 * @author medical-system
 */
@Data
public class AppointmentExportDTO {

    @ExcelProperty("预约单号")
    @ColumnWidth(20)
    private String appointmentNo;

    @ExcelProperty("患者姓名")
    @ColumnWidth(15)
    private String userName;

    @ExcelProperty("医生姓名")
    @ColumnWidth(15)
    private String doctorName;

    @ExcelProperty("科室名称")
    @ColumnWidth(15)
    private String departmentName;

    @ExcelProperty("预约日期")
    @ColumnWidth(15)
    private String appointmentDate;

    @ExcelProperty("预约时段")
    @ColumnWidth(15)
    private String timeSlot;

    @ExcelProperty("状态")
    @ColumnWidth(12)
    private String status;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private String createTime;
}
