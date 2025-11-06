package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical.entity.Schedule;
import com.medical.mapper.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班服务
 *
 * @author medical-system
 */
@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    /**
     * 获取医生的排班列表
     *
     * @param doctorId 医生ID
     * @return 排班列表
     */
    public List<Schedule> getDoctorSchedules(Long doctorId) {
        return scheduleMapper.selectList(
                new LambdaQueryWrapper<Schedule>()
                        .eq(Schedule::getDoctorId, doctorId)
                        .ge(Schedule::getWorkDate, LocalDate.now())
                        .eq(Schedule::getStatus, 1)
                        .orderByAsc(Schedule::getWorkDate)
                        .orderByAsc(Schedule::getTimeSlot)
        );
    }

    /**
     * 获取医生在指定日期的排班
     *
     * @param doctorId 医生ID
     * @param workDate 工作日期
     * @return 排班列表
     */
    public List<Schedule> getDoctorSchedulesByDate(Long doctorId, LocalDate workDate) {
        return scheduleMapper.selectList(
                new LambdaQueryWrapper<Schedule>()
                        .eq(Schedule::getDoctorId, doctorId)
                        .eq(Schedule::getWorkDate, workDate)
                        .eq(Schedule::getStatus, 1)
                        .orderByAsc(Schedule::getTimeSlot)
        );
    }

    /**
     * 检查排班是否可用
     *
     * @param scheduleId 排班ID
     * @return 是否可用
     */
    public boolean isScheduleAvailable(Long scheduleId) {
        Schedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule == null || schedule.getStatus() == 0) {
            return false;
        }
        return schedule.getBookedCount() < schedule.getMaxAppointments();
    }

    /**
     * 增加预约数量
     *
     * @param scheduleId 排班ID
     */
    public void incrementBookedCount(Long scheduleId) {
        Schedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule != null) {
            schedule.setBookedCount(schedule.getBookedCount() + 1);
            scheduleMapper.updateById(schedule);
        }
    }

    /**
     * 减少预约数量
     *
     * @param scheduleId 排班ID
     */
    public void decrementBookedCount(Long scheduleId) {
        Schedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule != null && schedule.getBookedCount() > 0) {
            schedule.setBookedCount(schedule.getBookedCount() - 1);
            scheduleMapper.updateById(schedule);
        }
    }
}
