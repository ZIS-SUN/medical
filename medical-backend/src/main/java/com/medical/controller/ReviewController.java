package com.medical.controller;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.dto.request.ReviewRequest;
import com.medical.dto.response.ReviewVO;
import com.medical.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 评价控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/reviews")
@Tag(name = "评价接口", description = "医生评价相关接口")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @Operation(summary = "提交评价")
    public Result<Void> createReview(
            @Validated @RequestBody ReviewRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        reviewService.createReview(userId, request);
        return Result.success();
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "查询医生的评价列表")
    public Result<PageResult<ReviewVO>> getDoctorReviews(
            @Parameter(description = "医生ID") @PathVariable Long doctorId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<ReviewVO> result = reviewService.getDoctorReviews(doctorId, page, pageSize);
        return Result.success(result);
    }

    @GetMapping("/doctor/{doctorId}/rating")
    @Operation(summary = "查询医生的平均评分")
    public Result<Map<String, Double>> getDoctorAverageRating(
            @Parameter(description = "医生ID") @PathVariable Long doctorId
    ) {
        Double rating = reviewService.getDoctorAverageRating(doctorId);
        Map<String, Double> data = new HashMap<>();
        data.put("averageRating", rating);
        return Result.success(data);
    }

    @GetMapping("/my")
    @Operation(summary = "查询我的评价列表")
    public Result<PageResult<ReviewVO>> getMyReviews(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        PageResult<ReviewVO> result = reviewService.getPatientReviews(userId, page, pageSize);
        return Result.success(result);
    }
}
