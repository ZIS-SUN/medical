package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical.common.BusinessException;
import com.medical.common.PageResult;
import com.medical.dto.request.ConsultationReplyRequest;
import com.medical.dto.request.ConsultationRequest;
import com.medical.dto.response.ConsultationVO;
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
 * 问诊服务
 *
 * @author medical-system
 */
@Service
public class ConsultationService {

    @Autowired
    private ConsultationMapper consultationMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 创建问诊
     */
    @Transactional(rollbackFor = Exception.class)
    public String createConsultation(Long userId, ConsultationRequest request) {
        // 1. 验证医生是否存在
        Doctor doctor = doctorMapper.selectById(request.getDoctorId());
        if (doctor == null) {
            throw new BusinessException(404, "医生不存在");
        }

        // 2. 生成问诊单号
        String consultationNo = generateConsultationNo();

        // 3. 创建问诊记录
        Consultation consultation = new Consultation();
        consultation.setConsultationNo(consultationNo);
        consultation.setUserId(userId);
        consultation.setDoctorId(request.getDoctorId());
        consultation.setQuestion(request.getQuestion());
        consultation.setQuestionImages(request.getQuestionImages());
        consultation.setStatus("待回复");
        consultation.setCreateTime(LocalDateTime.now());

        consultationMapper.insert(consultation);

        return consultationNo;
    }

    /**
     * 生成问诊单号
     */
    private String generateConsultationNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = consultationMapper.selectCount(null);
        return "CON" + date + String.format("%04d", count + 1);
    }

    /**
     * 查询我的问诊列表（患者端）
     */
    public PageResult<ConsultationVO> getMyConsultations(Long userId, String status,
                                                         Integer page, Integer pageSize) {
        LambdaQueryWrapper<Consultation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Consultation::getUserId, userId);

        if (status != null && !status.isEmpty()) {
            wrapper.eq(Consultation::getStatus, status);
        }

        wrapper.orderByDesc(Consultation::getCreateTime);

        Page<Consultation> pageParam = new Page<>(page, pageSize);
        Page<Consultation> pageResult = consultationMapper.selectPage(pageParam, wrapper);

        List<ConsultationVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 查询问诊详情
     */
    public ConsultationVO getConsultationDetail(Long consultationId, Long userId) {
        Consultation consultation = consultationMapper.selectById(consultationId);
        if (consultation == null) {
            throw new BusinessException(404, "问诊记录不存在");
        }

        // 权限校验（患者只能查看自己的问诊）
        if (!consultation.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权访问");
        }

        return convertToVO(consultation);
    }

    /**
     * 医生回复问诊
     */
    @Transactional(rollbackFor = Exception.class)
    public void replyConsultation(Long consultationId, Long doctorId,
                                  ConsultationReplyRequest request) {
        // 1. 查询问诊记录
        Consultation consultation = consultationMapper.selectById(consultationId);
        if (consultation == null) {
            throw new BusinessException(404, "问诊记录不存在");
        }

        // 2. 权限校验（只有对应的医生可以回复）
        if (!consultation.getDoctorId().equals(doctorId)) {
            throw new BusinessException(403, "无权操作");
        }

        // 3. 状态校验
        if ("已回复".equals(consultation.getStatus())) {
            throw new BusinessException(400, "该问诊已回复，无需重复回复");
        }

        // 4. 更新回复内容
        consultation.setAnswer(request.getAnswer());
        consultation.setStatus("已回复");
        consultation.setReplyTime(LocalDateTime.now());

        consultationMapper.updateById(consultation);
    }

    /**
     * 查询医生的问诊列表
     */
    public PageResult<ConsultationVO> getDoctorConsultations(Long doctorId, String status,
                                                             Integer page, Integer pageSize) {
        LambdaQueryWrapper<Consultation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Consultation::getDoctorId, doctorId);

        if (status != null && !status.isEmpty()) {
            wrapper.eq(Consultation::getStatus, status);
        }

        wrapper.orderByAsc(Consultation::getStatus)
                .orderByDesc(Consultation::getCreateTime);

        Page<Consultation> pageParam = new Page<>(page, pageSize);
        Page<Consultation> pageResult = consultationMapper.selectPage(pageParam, wrapper);

        List<ConsultationVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 管理端查询所有问诊列表
     */
    public PageResult<ConsultationVO> getAllConsultations(String keyword, String status,
                                                          Integer page, Integer pageSize) {
        LambdaQueryWrapper<Consultation> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Consultation::getConsultationNo, keyword)
                    .or().like(Consultation::getQuestion, keyword));
        }

        if (status != null && !status.isEmpty()) {
            wrapper.eq(Consultation::getStatus, status);
        }

        wrapper.orderByDesc(Consultation::getCreateTime);

        Page<Consultation> pageParam = new Page<>(page, pageSize);
        Page<Consultation> pageResult = consultationMapper.selectPage(pageParam, wrapper);

        List<ConsultationVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), voList);
    }

    /**
     * 删除问诊记录（管理端）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteConsultation(Long consultationId) {
        Consultation consultation = consultationMapper.selectById(consultationId);
        if (consultation == null) {
            throw new BusinessException(404, "问诊记录不存在");
        }

        consultationMapper.deleteById(consultationId);
    }

    /**
     * 转换为VO
     */
    private ConsultationVO convertToVO(Consultation consultation) {
        ConsultationVO vo = new ConsultationVO();
        BeanUtils.copyProperties(consultation, vo);

        // 查询患者姓名
        User user = userMapper.selectById(consultation.getUserId());
        if (user != null) {
            vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        }

        // 查询医生信息
        Doctor doctor = doctorMapper.selectById(consultation.getDoctorId());
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
