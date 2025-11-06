package com.medical.dto.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 评价导出DTO
 *
 * @author medical-system
 */
@Data
public class ReviewExportDTO {

    @ExcelProperty("患者姓名")
    @ColumnWidth(15)
    private String userName;

    @ExcelProperty("医生姓名")
    @ColumnWidth(15)
    private String doctorName;

    @ExcelProperty("评分")
    @ColumnWidth(10)
    private Integer rating;

    @ExcelProperty("评价内容")
    @ColumnWidth(40)
    private String content;

    @ExcelProperty("状态")
    @ColumnWidth(12)
    private String status;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private String createTime;
}
