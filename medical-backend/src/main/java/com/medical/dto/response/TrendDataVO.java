package com.medical.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 趋势数据响应对象
 *
 * @author medical-system
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "趋势数据响应对象")
public class TrendDataVO {

    @Schema(description = "日期")
    private String date;

    @Schema(description = "数量")
    private Long count;
}
