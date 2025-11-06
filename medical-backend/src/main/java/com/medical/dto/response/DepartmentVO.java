package com.medical.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 科室响应VO
 *
 * @author medical-system
 */
@Data
@Schema(description = "科室响应")
public class DepartmentVO {

    @Schema(description = "科室ID")
    private Long id;

    @Schema(description = "科室名称")
    private String name;

    @Schema(description = "父级科室ID")
    private Long parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "科室简介")
    private String description;

    @Schema(description = "子科室列表")
    private List<DepartmentVO> children;
}
