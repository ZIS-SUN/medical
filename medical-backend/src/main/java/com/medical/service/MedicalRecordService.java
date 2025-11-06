package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical.common.BusinessException;
import com.medical.common.PageResult;
import com.medical.dto.request.MedicalRecordRequest;
import com.medical.dto.response.MedicalRecordVO;
import com.medical.entity.*;
import com.medical.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 病历服务
 *
 * @author medical-system
 */
@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 创建病历
     */
    @Transactional(rollbackFor = Exception.class)
    public String createMedicalRecord(Long doctorId, MedicalRecordRequest request) {
        // 1. 验证预约是否存在
        Appointment appointment = appointmentMapper.selectById(request.getAppointmentId());
        if (appointment == null) {
            throw new BusinessException(404, "预约记录不存在");
        }

        // 2. 权限校验（只有预约的医生可以创建病历）
        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new BusinessException(403, "无权操作");
        }

        // 3. 检查是否已有病历
        Long count = medicalRecordMapper.selectCount(
                new LambdaQueryWrapper<MedicalRecord>()
                        .eq(MedicalRecord::getAppointmentId, request.getAppointmentId())
        );
        if (count > 0) {
            throw new BusinessException(400, "该预约已有病历记录");
        }

        // 4. 生成病历号
        String recordNo = generateRecordNo();

        // 5. 创建病历记录
        MedicalRecord record = new MedicalRecord();
        record.setRecordNo(recordNo);
        record.setAppointmentId(request.getAppointmentId());
        record.setUserId(request.getUserId());
        record.setDoctorId(doctorId);
        record.setChiefComplaint(request.getChiefComplaint());
        record.setPresentIllness(request.getPresentIllness());
        record.setDiagnosis(request.getDiagnosis());
        record.setPrescription(request.getPrescription());
        record.setAdvice(request.getAdvice());
        record.setCreateTime(LocalDateTime.now());

        medicalRecordMapper.insert(record);

        // 6. 更新预约状态为已完成
        appointment.setStatus("已完成");
        appointmentMapper.updateById(appointment);

        return recordNo;
    }

    /**
     * 生成病历号
     */
    private String generateRecordNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = medicalRecordMapper.selectCount(null);
        return "MR" + date + String.format("%04d", count + 1);
    }

    /**
     * 更新病历
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMedicalRecord(Long recordId, Long doctorId, MedicalRecordRequest request) {
        // 1. 查询病历记录
        MedicalRecord record = medicalRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(404, "病历记录不存在");
        }

        // 2. 权限校验（只有创建病历的医生可以修改）
        if (!record.getDoctorId().equals(doctorId)) {
            throw new BusinessException(403, "无权操作");
        }

        // 3. 更新病历信息
        record.setChiefComplaint(request.getChiefComplaint());
        record.setPresentIllness(request.getPresentIllness());
        record.setDiagnosis(request.getDiagnosis());
        record.setPrescription(request.getPrescription());
        record.setAdvice(request.getAdvice());

        medicalRecordMapper.updateById(record);
    }

    /**
     * 查询患者的病历列表
     */
    public PageResult<MedicalRecordVO> getPatientRecords(Long userId, Integer page, Integer pageSize) {
        LambdaQueryWrapper<MedicalRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicalRecord::getUserId, userId)
                .orderByDesc(MedicalRecord::getCreateTime);

        Page<MedicalRecord> pageParam = new Page<>(page, pageSize);
        Page<MedicalRecord> pageResult = medicalRecordMapper.selectPage(pageParam, wrapper);

        List<MedicalRecordVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 查询病历详情
     */
    public MedicalRecordVO getRecordDetail(Long recordId, Long userId) {
        MedicalRecord record = medicalRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(404, "病历记录不存在");
        }

        // 权限校验（患者只能查看自己的病历）
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权访问");
        }

        return convertToVO(record);
    }

    /**
     * 查询医生的病历列表
     */
    public PageResult<MedicalRecordVO> getDoctorRecords(Long doctorId, Integer page, Integer pageSize) {
        LambdaQueryWrapper<MedicalRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicalRecord::getDoctorId, doctorId)
                .orderByDesc(MedicalRecord::getCreateTime);

        Page<MedicalRecord> pageParam = new Page<>(page, pageSize);
        Page<MedicalRecord> pageResult = medicalRecordMapper.selectPage(pageParam, wrapper);

        List<MedicalRecordVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 管理端查询所有病历列表
     */
    public PageResult<MedicalRecordVO> getAllRecords(String keyword, Integer page, Integer pageSize) {
        LambdaQueryWrapper<MedicalRecord> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(MedicalRecord::getRecordNo, keyword)
                    .or().like(MedicalRecord::getDiagnosis, keyword));
        }

        wrapper.orderByDesc(MedicalRecord::getCreateTime);

        Page<MedicalRecord> pageParam = new Page<>(page, pageSize);
        Page<MedicalRecord> pageResult = medicalRecordMapper.selectPage(pageParam, wrapper);

        List<MedicalRecordVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 删除病历记录（管理端）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecord(Long recordId) {
        MedicalRecord record = medicalRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(404, "病历记录不存在");
        }

        medicalRecordMapper.deleteById(recordId);
    }

    /**
     * 转换为VO
     */
    private MedicalRecordVO convertToVO(MedicalRecord record) {
        MedicalRecordVO vo = new MedicalRecordVO();
        BeanUtils.copyProperties(record, vo);

        // 查询预约信息
        Appointment appointment = appointmentMapper.selectById(record.getAppointmentId());
        if (appointment != null) {
            vo.setAppointmentNo(appointment.getAppointmentNo());
        }

        // 查询患者姓名
        User user = userMapper.selectById(record.getUserId());
        if (user != null) {
            vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        }

        // 查询医生信息
        Doctor doctor = doctorMapper.selectById(record.getDoctorId());
        if (doctor != null) {
            vo.setDoctorName(doctor.getName());

            // 查询科室名称
            Department department = departmentMapper.selectById(doctor.getDepartmentId());
            if (department != null) {
                vo.setDepartmentName(department.getName());
            }
        }

        return vo;
    }
}
