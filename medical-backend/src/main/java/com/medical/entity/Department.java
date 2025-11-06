package com.medical.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 科室实体
 *
 * @author medical-system
 */
@Data
@TableName("department")
@Schema(description = "科室实体")
public class Department {

    @TableId(type = IdType.AUTO)
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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
