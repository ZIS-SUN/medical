package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical.common.BusinessException;
import com.medical.common.PageResult;
import com.medical.dto.response.DoctorVO;
import com.medical.entity.Department;
import com.medical.entity.Doctor;
import com.medical.entity.Review;
import com.medical.mapper.DepartmentMapper;
import com.medical.mapper.DoctorMapper;
import com.medical.mapper.ReviewMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医生服务
 *
 * @author medical-system
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * 分页查询医生列表
     */
    public PageResult<DoctorVO> getDoctorList(Long departmentId, String title,
                                               String keyword, Integer page, Integer pageSize) {
        // 构建查询条件
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getStatus, 1); // 只查询在职医生

        if (departmentId != null) {
            wrapper.eq(Doctor::getDepartmentId, departmentId);
        }
        if (title != null && !title.isEmpty()) {
            wrapper.eq(Doctor::getTitle, title);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Doctor::getName, keyword);
        }

        // 分页查询
        Page<Doctor> pageParam = new Page<>(page, pageSize);
        Page<Doctor> pageResult = doctorMapper.selectPage(pageParam, wrapper);

        // 转换为VO
        List<DoctorVO> voList = pageResult.getRecords().stream().map(doctor -> {
            DoctorVO vo = convertToVO(doctor);
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 获取医生详情
     */
    public DoctorVO getDoctorDetail(Long id) {
        Doctor doctor = doctorMapper.selectById(id);
        if (doctor == null) {
            throw new BusinessException(404, "医生不存在");
        }

        return convertToVO(doctor);
    }

    /**
     * 获取热门医生列表（根据评分排序）
     */
    public List<DoctorVO> getHotDoctors() {
        // 查询所有在职医生
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getStatus, 1);
        wrapper.last("LIMIT 10"); // 限制返回10个

        List<Doctor> doctors = doctorMapper.selectList(wrapper);

        // 转换为VO并排序
        List<DoctorVO> voList = doctors.stream()
                .map(this::convertToVO)
                .sorted((a, b) -> Double.compare(b.getAvgRating(), a.getAvgRating()))
                .collect(Collectors.toList());

        return voList;
    }

    /**
     * 转换为VO
     */
    private DoctorVO convertToVO(Doctor doctor) {
        DoctorVO vo = new DoctorVO();
        BeanUtils.copyProperties(doctor, vo);

        // 查询科室名称
        Department department = departmentMapper.selectById(doctor.getDepartmentId());
        if (department != null) {
            vo.setDepartmentName(department.getName());
        }

        // 计算平均评分和评价数量
        List<Review> reviews = reviewMapper.selectList(
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getDoctorId, doctor.getId())
                        .eq(Review::getStatus, 1)
        );

        if (!reviews.isEmpty()) {
            double avgRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            vo.setAvgRating(Math.round(avgRating * 10) / 10.0); // 保留一位小数
            vo.setReviewCount((long) reviews.size());
        } else {
            vo.setAvgRating(0.0);
            vo.setReviewCount(0L);
        }

        return vo;
    }
}
