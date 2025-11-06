package com.medical.controller.admin;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.dto.response.ReviewVO;
import com.medical.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端评价控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/reviews")
@Tag(name = "管理端-评价管理", description = "管理端评价管理接口")
public class AdminReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    @Operation(summary = "查询所有评价列表")
    public Result<PageResult<ReviewVO>> getAllReviews(
            @Parameter(description = "关键词搜索") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态筛选") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<ReviewVO> result = reviewService.getAllReviews(
                keyword, status, page, pageSize
        );
        return Result.success(result);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新评价状态")
    public Result<Void> updateReviewStatus(
            @Parameter(description = "评价ID") @PathVariable Long id,
            @Parameter(description = "状态（0-隐藏，1-显示）") @RequestParam Integer status
    ) {
        reviewService.updateReviewStatus(id, status);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评价记录")
    public Result<Void> deleteReview(
            @Parameter(description = "评价ID") @PathVariable Long id
    ) {
        reviewService.deleteReview(id);
        return Result.success();
    }
}
