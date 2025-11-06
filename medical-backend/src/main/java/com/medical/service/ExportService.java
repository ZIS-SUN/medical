package com.medical.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical.dto.export.AppointmentExportDTO;
import com.medical.dto.export.ConsultationExportDTO;
import com.medical.dto.export.MedicalRecordExportDTO;
import com.medical.dto.export.ReviewExportDTO;
import com.medical.entity.*;
import com.medical.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 导出服务
 *
 * @author medical-system
 */
@Service
public class ExportService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private ConsultationMapper consultationMapper;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 导出预约数据
     */
    public void exportAppointments(HttpServletResponse response, String status) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("预约数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 查询数据
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Appointment::getStatus, status);
        }
        wrapper.orderByDesc(Appointment::getCreateTime);

        List<Appointment> appointments = appointmentMapper.selectList(wrapper);

        // 转换为导出DTO
        List<AppointmentExportDTO> exportData = appointments.stream()
                .map(this::convertToAppointmentExportDTO)
                .collect(Collectors.toList());

        // 写入Excel
        EasyExcel.write(response.getOutputStream(), AppointmentExportDTO.class)
                .sheet("预约数据")
                .doWrite(exportData);
    }

    /**
     * 导出问诊数据
     */
    public void exportConsultations(HttpServletResponse response, String status) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("问诊数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        LambdaQueryWrapper<Consultation> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Consultation::getStatus, status);
        }
        wrapper.orderByDesc(Consultation::getCreateTime);

        List<Consultation> consultations = consultationMapper.selectList(wrapper);

        List<ConsultationExportDTO> exportData = consultations.stream()
                .map(this::convertToConsultationExportDTO)
                .collect(Collectors.toList());

        EasyExcel.write(response.getOutputStream(), ConsultationExportDTO.class)
                .sheet("问诊数据")
                .doWrite(exportData);
    }

    /**
     * 导出病历数据
     */
    public void exportMedicalRecords(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("病历数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        LambdaQueryWrapper<MedicalRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(MedicalRecord::getCreateTime);

        List<MedicalRecord> records = medicalRecordMapper.selectList(wrapper);

        List<MedicalRecordExportDTO> exportData = records.stream()
                .map(this::convertToMedicalRecordExportDTO)
                .collect(Collectors.toList());

        EasyExcel.write(response.getOutputStream(), MedicalRecordExportDTO.class)
                .sheet("病历数据")
                .doWrite(exportData);
    }

    /**
     * 导出评价数据
     */
    public void exportReviews(HttpServletResponse response, Integer status) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("评价数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Review::getStatus, status);
        }
        wrapper.orderByDesc(Review::getCreateTime);

        List<Review> reviews = reviewMapper.selectList(wrapper);

        List<ReviewExportDTO> exportData = reviews.stream()
                .map(this::convertToReviewExportDTO)
                .collect(Collectors.toList());

        EasyExcel.write(response.getOutputStream(), ReviewExportDTO.class)
                .sheet("评价数据")
                .doWrite(exportData);
    }

    /**
     * 转换预约为导出DTO
     */
    private AppointmentExportDTO convertToAppointmentExportDTO(Appointment appointment) {
        AppointmentExportDTO dto = new AppointmentExportDTO();
        dto.setAppointmentNo(appointment.getAppointmentNo());

        User user = userMapper.selectById(appointment.getUserId());
        if (user != null) {
            dto.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        }

        Doctor doctor = doctorMapper.selectById(appointment.getDoctorId());
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());

            Department department = departmentMapper.selectById(doctor.getDepartmentId());
            if (department != null) {
                dto.setDepartmentName(department.getName());
            }
        }

        dto.setAppointmentDate(appointment.getAppointmentDate().format(DATE_FORMATTER));
        dto.setTimeSlot(appointment.getTimeSlot());
        dto.setStatus(appointment.getStatus());
        dto.setCreateTime(appointment.getCreateTime().format(DATETIME_FORMATTER));

        return dto;
    }

    /**
     * 转换问诊为导出DTO
     */
    private ConsultationExportDTO convertToConsultationExportDTO(Consultation consultation) {
        ConsultationExportDTO dto = new ConsultationExportDTO();
        dto.setConsultationNo(consultation.getConsultationNo());

        User user = userMapper.selectById(consultation.getUserId());
        if (user != null) {
            dto.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        }

        Doctor doctor = doctorMapper.selectById(consultation.getDoctorId());
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());

            Department department = departmentMapper.selectById(doctor.getDepartmentId());
            if (department != null) {
                dto.setDepartmentName(department.getName());
            }
        }

        dto.setQuestion(consultation.getQuestion());
        dto.setAnswer(consultation.getAnswer());
        dto.setStatus(consultation.getStatus());
        dto.setCreateTime(consultation.getCreateTime().format(DATETIME_FORMATTER));

        return dto;
    }

    /**
     * 转换病历为导出DTO
     */
    private MedicalRecordExportDTO convertToMedicalRecordExportDTO(MedicalRecord record) {
        MedicalRecordExportDTO dto = new MedicalRecordExportDTO();
        dto.setRecordNo(record.getRecordNo());

        User user = userMapper.selectById(record.getUserId());
        if (user != null) {
            dto.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        }

        Doctor doctor = doctorMapper.selectById(record.getDoctorId());
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());

            Department department = departmentMapper.selectById(doctor.getDepartmentId());
            if (department != null) {
                dto.setDepartmentName(department.getName());
            }
        }

        dto.setChiefComplaint(record.getChiefComplaint());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setPrescription(record.getPrescription());
        dto.setCreateTime(record.getCreateTime().format(DATETIME_FORMATTER));

        return dto;
    }

    /**
     * 转换评价为导出DTO
     */
    private ReviewExportDTO convertToReviewExportDTO(Review review) {
        ReviewExportDTO dto = new ReviewExportDTO();

        User user = userMapper.selectById(review.getUserId());
        if (user != null) {
            dto.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        }

        Doctor doctor = doctorMapper.selectById(review.getDoctorId());
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());
        }

        dto.setRating(review.getRating());
        dto.setContent(review.getContent());
        dto.setStatus(review.getStatus() == 1 ? "显示" : "隐藏");
        dto.setCreateTime(review.getCreateTime().format(DATETIME_FORMATTER));

        return dto;
    }
}
