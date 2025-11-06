package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical.common.BusinessException;
import com.medical.common.PageResult;
import com.medical.dto.request.AppointmentRequest;
import com.medical.dto.response.AppointmentVO;
import com.medical.entity.*;
import com.medical.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预约服务
 *
 * @author medical-system
 */
@Service
public class AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    /**
     * 创建预约
     */
    @Transactional(rollbackFor = Exception.class)
    public String createAppointment(Long userId, AppointmentRequest request) {
        // 1. 验证预约
        validateAppointment(userId, request);

        // 2. 查询医生信息
        Doctor doctor = doctorMapper.selectById(request.getDoctorId());
        if (doctor == null) {
            throw new BusinessException(404, "医生不存在");
        }

        // 3. 生成预约单号
        String appointmentNo = generateAppointmentNo();

        // 4. 创建预约记录
        Appointment appointment = new Appointment();
        appointment.setAppointmentNo(appointmentNo);
        appointment.setUserId(userId);
        appointment.setDoctorId(request.getDoctorId());
        appointment.setDepartmentId(doctor.getDepartmentId());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setTimeSlot(request.getTimeSlot());
        appointment.setStatus("待确认");
        appointment.setRemark(request.getRemark());

        appointmentMapper.insert(appointment);

        // 5. 更新排班表已预约数
        updateScheduleBookedCount(request.getDoctorId(),
                request.getAppointmentDate(), request.getTimeSlot(), 1);

        return appointmentNo;
    }

    /**
     * 验证预约
     */
    private void validateAppointment(Long userId, AppointmentRequest request) {
        // 1. 检查是否重复预约
        Long count = appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getUserId, userId)
                        .eq(Appointment::getDoctorId, request.getDoctorId())
                        .eq(Appointment::getAppointmentDate, request.getAppointmentDate())
                        .in(Appointment::getStatus, Arrays.asList("待确认", "已确认", "就诊中"))
        );
        if (count > 0) {
            throw new BusinessException(20002, "您已预约该医生，请勿重复预约");
        }

        // 2. 检查时段是否已满
        Schedule schedule = scheduleMapper.selectOne(
                new LambdaQueryWrapper<Schedule>()
                        .eq(Schedule::getDoctorId, request.getDoctorId())
                        .eq(Schedule::getWorkDate, request.getAppointmentDate())
                        .eq(Schedule::getTimeSlot, request.getTimeSlot())
        );

        if (schedule == null || schedule.getStatus() == 0) {
            throw new BusinessException(20001, "该时段医生未排班");
        }

        if (schedule.getBookedCount() >= schedule.getMaxAppointments()) {
            throw new BusinessException(20001, "该时段预约已满");
        }

        // 3. 检查预约日期是否合法（只能预约未来7天内）
        LocalDate today = LocalDate.now();
        LocalDate appointmentDate = request.getAppointmentDate();

        if (appointmentDate.isBefore(today)) {
            throw new BusinessException(400, "不能预约过去的日期");
        }

        if (appointmentDate.isAfter(today.plusDays(7))) {
            throw new BusinessException(400, "只能预约未来7天内的时段");
        }
    }

    /**
     * 生成预约单号
     */
    private String generateAppointmentNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = appointmentMapper.selectCount(null);
        return "APT" + date + String.format("%03d", count + 1);
    }

    /**
     * 更新排班已预约数
     */
    private void updateScheduleBookedCount(Long doctorId, LocalDate workDate,
                                           String timeSlot, int delta) {
        Schedule schedule = scheduleMapper.selectOne(
                new LambdaQueryWrapper<Schedule>()
                        .eq(Schedule::getDoctorId, doctorId)
                        .eq(Schedule::getWorkDate, workDate)
                        .eq(Schedule::getTimeSlot, timeSlot)
        );

        if (schedule != null) {
            schedule.setBookedCount(schedule.getBookedCount() + delta);
            scheduleMapper.updateById(schedule);
        }
    }

    /**
     * 查询我的预约列表
     */
    public PageResult<AppointmentVO> getMyAppointments(Long userId, String status,
                                                       Integer page, Integer pageSize) {
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Appointment::getUserId, userId);

        if (status != null && !status.isEmpty()) {
            wrapper.eq(Appointment::getStatus, status);
        }

        wrapper.orderByDesc(Appointment::getCreateTime);

        Page<Appointment> pageParam = new Page<>(page, pageSize);
        Page<Appointment> pageResult = appointmentMapper.selectPage(pageParam, wrapper);

        List<AppointmentVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 查询所有预约列表（管理端）
     */
    public PageResult<AppointmentVO> getAppointmentList(String status, Integer page, Integer pageSize) {
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();

        if (status != null && !status.isEmpty()) {
            wrapper.eq(Appointment::getStatus, status);
        }

        wrapper.orderByDesc(Appointment::getCreateTime);

        Page<Appointment> pageParam = new Page<>(page, pageSize);
        Page<Appointment> pageResult = appointmentMapper.selectPage(pageParam, wrapper);

        List<AppointmentVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 取消预约
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelAppointment(Long appointmentId, Long userId, String cancelReason) {
        // 1. 查询预约记录
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new BusinessException(404, "预约记录不存在");
        }

        // 2. 权限校验
        if (!appointment.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作");
        }

        // 3. 状态校验
        if ("已取消".equals(appointment.getStatus()) ||
                "已完成".equals(appointment.getStatus())) {
            throw new BusinessException(20003, "该预约无法取消");
        }

        // 4. 更新预约状态
        appointment.setStatus("已取消");
        appointment.setCancelReason(cancelReason);
        appointmentMapper.updateById(appointment);

        // 5. 减少排班已预约数
        updateScheduleBookedCount(
                appointment.getDoctorId(),
                appointment.getAppointmentDate(),
                appointment.getTimeSlot(),
                -1
        );
    }

    /**
     * 转换为VO
     */
    private AppointmentVO convertToVO(Appointment appointment) {
        AppointmentVO vo = new AppointmentVO();
        BeanUtils.copyProperties(appointment, vo);

        // 查询患者姓名
        User user = userMapper.selectById(appointment.getUserId());
        if (user != null) {
            vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        }

        // 查询医生姓名
        Doctor doctor = doctorMapper.selectById(appointment.getDoctorId());
        if (doctor != null) {
            vo.setDoctorName(doctor.getName());
        }

        // 查询科室名称
        Department department = departmentMapper.selectById(appointment.getDepartmentId());
        if (department != null) {
            vo.setDepartmentName(department.getName());
        }

        return vo;
    }
}
