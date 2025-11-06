package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical.common.BusinessException;
import com.medical.common.PageResult;
import com.medical.dto.request.ReviewRequest;
import com.medical.dto.response.ReviewVO;
import com.medical.entity.*;
import com.medical.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价服务
 *
 * @author medical-system
 */
@Service
public class ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 创建评价
     */
    @Transactional(rollbackFor = Exception.class)
    public void createReview(Long userId, ReviewRequest request) {
        // 1. 验证预约是否存在
        Appointment appointment = appointmentMapper.selectById(request.getAppointmentId());
        if (appointment == null) {
            throw new BusinessException(404, "预约记录不存在");
        }

        // 2. 权限校验（只有预约的患者可以评价）
        if (!appointment.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作");
        }

        // 3. 状态校验（只有已完成的预约才能评价）
        if (!"已完成".equals(appointment.getStatus())) {
            throw new BusinessException(400, "只能对已完成的预约进行评价");
        }

        // 4. 检查是否已评价
        Long count = reviewMapper.selectCount(
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getUserId, userId)
                        .eq(Review::getAppointmentId, request.getAppointmentId())
        );
        if (count > 0) {
            throw new BusinessException(400, "您已对该预约进行过评价");
        }

        // 5. 验证医生是否存在
        Doctor doctor = doctorMapper.selectById(request.getDoctorId());
        if (doctor == null) {
            throw new BusinessException(404, "医生不存在");
        }

        // 6. 创建评价记录
        Review review = new Review();
        review.setUserId(userId);
        review.setDoctorId(request.getDoctorId());
        review.setAppointmentId(request.getAppointmentId());
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setStatus(1); // 默认显示
        review.setCreateTime(LocalDateTime.now());

        reviewMapper.insert(review);
    }

    /**
     * 查询医生的评价列表
     */
    public PageResult<ReviewVO> getDoctorReviews(Long doctorId, Integer page, Integer pageSize) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getDoctorId, doctorId)
                .eq(Review::getStatus, 1) // 只显示状态为1的评价
                .orderByDesc(Review::getCreateTime);

        Page<Review> pageParam = new Page<>(page, pageSize);
        Page<Review> pageResult = reviewMapper.selectPage(pageParam, wrapper);

        List<ReviewVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 查询患者的评价列表
     */
    public PageResult<ReviewVO> getPatientReviews(Long userId, Integer page, Integer pageSize) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getUserId, userId)
                .orderByDesc(Review::getCreateTime);

        Page<Review> pageParam = new Page<>(page, pageSize);
        Page<Review> pageResult = reviewMapper.selectPage(pageParam, wrapper);

        List<ReviewVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 管理端查询所有评价列表
     */
    public PageResult<ReviewVO> getAllReviews(String keyword, Integer status,
                                              Integer page, Integer pageSize) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Review::getContent, keyword);
        }

        if (status != null) {
            wrapper.eq(Review::getStatus, status);
        }

        wrapper.orderByDesc(Review::getCreateTime);

        Page<Review> pageParam = new Page<>(page, pageSize);
        Page<Review> pageResult = reviewMapper.selectPage(pageParam, wrapper);

        List<ReviewVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 更新评价状态（管理端：显示/隐藏）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateReviewStatus(Long reviewId, Integer status) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException(404, "评价记录不存在");
        }

        review.setStatus(status);
        reviewMapper.updateById(review);
    }

    /**
     * 删除评价（管理端）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteReview(Long reviewId) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException(404, "评价记录不存在");
        }

        reviewMapper.deleteById(reviewId);
    }

    /**
     * 计算医生的平均评分
     */
    public Double getDoctorAverageRating(Long doctorId) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getDoctorId, doctorId)
                .eq(Review::getStatus, 1);

        List<Review> reviews = reviewMapper.selectList(wrapper);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        double sum = reviews.stream()
                .mapToInt(Review::getRating)
                .sum();

        return Math.round(sum / reviews.size() * 10.0) / 10.0; // 保留一位小数
    }

    /**
     * 转换为VO
     */
    private ReviewVO convertToVO(Review review) {
        ReviewVO vo = new ReviewVO();
        BeanUtils.copyProperties(review, vo);

        // 查询患者姓名
        User user = userMapper.selectById(review.getUserId());
        if (user != null) {
            vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        }

        // 查询医生姓名
        Doctor doctor = doctorMapper.selectById(review.getDoctorId());
        if (doctor != null) {
            vo.setDoctorName(doctor.getName());
        }

        // 查询预约信息
        Appointment appointment = appointmentMapper.selectById(review.getAppointmentId());
        if (appointment != null) {
            vo.setAppointmentNo(appointment.getAppointmentNo());
        }

        return vo;
    }
}
