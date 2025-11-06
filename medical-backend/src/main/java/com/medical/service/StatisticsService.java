package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical.dto.response.StatisticsVO;
import com.medical.dto.response.TrendDataVO;
import com.medical.entity.*;
import com.medical.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计服务
 *
 * @author medical-system
 */
@Service
public class StatisticsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private ConsultationMapper consultationMapper;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * 获取统计数据
     */
    public StatisticsVO getStatistics() {
        StatisticsVO vo = new StatisticsVO();

        // 基础统计
        vo.setTotalUsers(userMapper.selectCount(null));
        vo.setTotalDoctors(doctorMapper.selectCount(null));
        vo.setTotalAppointments(appointmentMapper.selectCount(null));
        vo.setTotalConsultations(consultationMapper.selectCount(null));
        vo.setTotalRecords(medicalRecordMapper.selectCount(null));
        vo.setTotalReviews(reviewMapper.selectCount(null));

        // 今日统计
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        vo.setTodayAppointments(appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .between(Appointment::getCreateTime, startOfDay, endOfDay)
        ));

        vo.setTodayConsultations(consultationMapper.selectCount(
                new LambdaQueryWrapper<Consultation>()
                        .between(Consultation::getCreateTime, startOfDay, endOfDay)
        ));

        // 待处理统计
        vo.setPendingAppointments(appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getStatus, "待确认")
        ));

        vo.setPendingConsultations(consultationMapper.selectCount(
                new LambdaQueryWrapper<Consultation>()
                        .eq(Consultation::getStatus, "待回复")
        ));

        return vo;
    }

    /**
     * 获取预约趋势数据（最近7天）
     */
    public List<TrendDataVO> getAppointmentTrend() {
        List<TrendDataVO> trendData = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            Long count = appointmentMapper.selectCount(
                    new LambdaQueryWrapper<Appointment>()
                            .between(Appointment::getCreateTime, startOfDay, endOfDay)
            );

            trendData.add(new TrendDataVO(date.format(formatter), count));
        }

        return trendData;
    }

    /**
     * 获取问诊趋势数据（最近7天）
     */
    public List<TrendDataVO> getConsultationTrend() {
        List<TrendDataVO> trendData = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            Long count = consultationMapper.selectCount(
                    new LambdaQueryWrapper<Consultation>()
                            .between(Consultation::getCreateTime, startOfDay, endOfDay)
            );

            trendData.add(new TrendDataVO(date.format(formatter), count));
        }

        return trendData;
    }

    /**
     * 获取预约状态分布
     */
    public List<TrendDataVO> getAppointmentStatusDistribution() {
        List<TrendDataVO> distribution = new ArrayList<>();

        String[] statuses = {"待确认", "已确认", "就诊中", "已完成", "已取消"};
        for (String status : statuses) {
            Long count = appointmentMapper.selectCount(
                    new LambdaQueryWrapper<Appointment>()
                            .eq(Appointment::getStatus, status)
            );
            distribution.add(new TrendDataVO(status, count));
        }

        return distribution;
    }

    /**
     * 获取问诊状态分布
     */
    public List<TrendDataVO> getConsultationStatusDistribution() {
        List<TrendDataVO> distribution = new ArrayList<>();

        String[] statuses = {"待回复", "已回复"};
        for (String status : statuses) {
            Long count = consultationMapper.selectCount(
                    new LambdaQueryWrapper<Consultation>()
                            .eq(Consultation::getStatus, status)
            );
            distribution.add(new TrendDataVO(status, count));
        }

        return distribution;
    }
}
