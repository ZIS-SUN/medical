-- 创建数据库
CREATE DATABASE IF NOT EXISTS medical_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE medical_db;

-- 1. 用户表
CREATE TABLE `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
  `password` VARCHAR(128) NOT NULL COMMENT '密码（BCrypt加密）',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知 1-男 2-女',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT '/uploads/avatar/default.png' COMMENT '头像',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX idx_phone (`phone`),
  INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者用户表';

-- 2. 医生表
CREATE TABLE `doctor` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '医生姓名',
  `gender` TINYINT COMMENT '性别：1-男 2-女',
  `title` VARCHAR(50) COMMENT '职称：主任医师/副主任医师/主治医师',
  `department_id` BIGINT NOT NULL COMMENT '科室ID',
  `avatar` VARCHAR(255) COMMENT '头像',
  `profile` TEXT COMMENT '医生简介',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-离职 1-在职',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_department_id (`department_id`),
  INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生表';

-- 3. 科室表
CREATE TABLE `department` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '科室名称',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父级科室ID，0为一级科室',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `description` TEXT COMMENT '科室简介',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_parent_id (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室表';

-- 4. 预约表
CREATE TABLE `appointment` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `appointment_no` VARCHAR(32) UNIQUE COMMENT '预约单号：APT+日期+流水号',
  `user_id` BIGINT NOT NULL COMMENT '患者ID',
  `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
  `department_id` BIGINT NOT NULL COMMENT '科室ID',
  `appointment_date` DATE NOT NULL COMMENT '预约日期',
  `time_slot` VARCHAR(20) NOT NULL COMMENT '时间段：上午/下午',
  `status` VARCHAR(20) DEFAULT '待确认' COMMENT '状态：待确认/已确认/就诊中/已完成/已取消',
  `cancel_reason` VARCHAR(255) COMMENT '取消原因',
  `remark` VARCHAR(255) COMMENT '备注（患者填写）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_user_id (`user_id`),
  INDEX idx_doctor_id (`doctor_id`),
  INDEX idx_appointment_date (`appointment_date`),
  INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 5. 问诊表
CREATE TABLE `consultation` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `consultation_no` VARCHAR(32) UNIQUE COMMENT '问诊单号：CON+日期+流水号',
  `user_id` BIGINT NOT NULL COMMENT '患者ID',
  `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
  `question` TEXT NOT NULL COMMENT '患者问题',
  `question_images` VARCHAR(500) COMMENT '问题图片，多个用逗号分隔',
  `answer` TEXT COMMENT '医生回复',
  `status` VARCHAR(20) DEFAULT '待回复' COMMENT '状态：待回复/已回复/已关闭',
  `reply_time` DATETIME COMMENT '回复时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_id (`user_id`),
  INDEX idx_doctor_id (`doctor_id`),
  INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问诊表';

-- 6. 病历表
CREATE TABLE `medical_record` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `record_no` VARCHAR(32) UNIQUE COMMENT '病历号：MR+日期+流水号',
  `appointment_id` BIGINT NOT NULL COMMENT '预约ID',
  `user_id` BIGINT NOT NULL COMMENT '患者ID',
  `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
  `chief_complaint` VARCHAR(500) COMMENT '主诉',
  `present_illness` TEXT COMMENT '现病史',
  `diagnosis` TEXT NOT NULL COMMENT '诊断结果',
  `prescription` TEXT COMMENT '处方内容',
  `advice` TEXT COMMENT '医嘱建议',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_id (`user_id`),
  INDEX idx_appointment_id (`appointment_id`),
  INDEX idx_doctor_id (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病历表';

-- 7. 评价表
CREATE TABLE `review` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '患者ID',
  `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
  `appointment_id` BIGINT COMMENT '预约ID',
  `rating` TINYINT NOT NULL COMMENT '评分：1-5星',
  `content` TEXT COMMENT '评价内容',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-隐藏 1-显示',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_doctor_id (`doctor_id`),
  INDEX idx_user_id (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 8. 管理员表
CREATE TABLE `admin` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '管理员账号',
  `password` VARCHAR(128) NOT NULL COMMENT '密码（BCrypt加密）',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `role` VARCHAR(20) DEFAULT 'admin' COMMENT '角色：admin-管理员 doctor-医生',
  `doctor_id` BIGINT COMMENT '关联医生ID（医生角色使用）',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_role (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 9. 公告表
CREATE TABLE `announcement` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
  `content` TEXT NOT NULL COMMENT '公告内容',
  `type` VARCHAR(20) DEFAULT 'notice' COMMENT '类型：notice-通知 health-健康科普',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架 1-发布',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `publish_time` DATETIME COMMENT '发布时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_status (`status`),
  INDEX idx_type (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 10. 医生排班表
CREATE TABLE `schedule` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
  `work_date` DATE NOT NULL COMMENT '排班日期',
  `time_slot` VARCHAR(20) NOT NULL COMMENT '时间段：上午/下午',
  `max_appointments` INT DEFAULT 20 COMMENT '最大预约数',
  `booked_count` INT DEFAULT 0 COMMENT '已预约数',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-停诊 1-正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_doctor_date_slot (`doctor_id`, `work_date`, `time_slot`),
  INDEX idx_work_date (`work_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生排班表';

-- =====================
-- 初始化测试数据
-- =====================

-- 1. 科室数据
INSERT INTO `department` (id, name, parent_id, sort, description) VALUES
(1, '内科', 0, 1, '内科疾病诊疗'),
(11, '心内科', 1, 1, '心血管疾病诊疗'),
(12, '消化内科', 1, 2, '消化系统疾病诊疗'),
(13, '呼吸内科', 1, 3, '呼吸系统疾病诊疗'),
(2, '外科', 0, 2, '外科疾病诊疗'),
(21, '骨科', 2, 1, '骨骼系统疾病诊疗'),
(22, '普外科', 2, 2, '普通外科疾病诊疗'),
(3, '儿科', 0, 3, '儿童疾病诊疗'),
(4, '妇产科', 0, 4, '妇产科疾病诊疗');

-- 2. 医生数据
INSERT INTO `doctor` (name, gender, title, department_id, avatar, profile) VALUES
('李明', 1, '主任医师', 11, '/uploads/doctor/default.png', '从事心血管疾病诊疗20余年，擅长高血压、冠心病的诊治'),
('王芳', 2, '副主任医师', 12, '/uploads/doctor/default.png', '消化内科专家，擅长胃肠道疾病的诊治'),
('张强', 1, '主治医师', 21, '/uploads/doctor/default.png', '骨科运动损伤专家，擅长关节疾病的诊治'),
('刘丽', 2, '主任医师', 13, '/uploads/doctor/default.png', '呼吸内科专家，擅长哮喘、肺炎的诊治'),
('陈伟', 1, '副主任医师', 22, '/uploads/doctor/default.png', '普外科专家，擅长腹腔镜手术'),
('赵敏', 2, '主治医师', 3, '/uploads/doctor/default.png', '儿科专家，擅长儿童常见病的诊治'),
('孙涛', 1, '主任医师', 4, '/uploads/doctor/default.png', '妇产科专家，擅长产前检查和妇科疾病诊治');

-- 3. 管理员账号和医生账号（密码：admin123，BCrypt加密）
-- 密码明文：admin123
-- BCrypt加密后 (使用 src/test/java/PasswordGenerator.java 生成)：
-- $2a$10$Y6nNzuuYMlyISs/TrdxrqeFkiMrW7zanFHDMsC.Pa59uPA0oUs7me
INSERT INTO `admin` (username, password, real_name, role, doctor_id, status) VALUES
('admin', '$2a$10$Y6nNzuuYMlyISs/TrdxrqeFkiMrW7zanFHDMsC.Pa59uPA0oUs7me', '系统管理员', 'admin', NULL, 1),
('doctor01', '$2a$10$Y6nNzuuYMlyISs/TrdxrqeFkiMrW7zanFHDMsC.Pa59uPA0oUs7me', '李明', 'doctor', 1, 1),
('doctor02', '$2a$10$Y6nNzuuYMlyISs/TrdxrqeFkiMrW7zanFHDMsC.Pa59uPA0oUs7me', '王芳', 'doctor', 2, 1),
('doctor03', '$2a$10$Y6nNzuuYMlyISs/TrdxrqeFkiMrW7zanFHDMsC.Pa59uPA0oUs7me', '张强', 'doctor', 3, 1);

-- 4. 医生排班数据（未来7天）
-- 为第一位医生（李明）创建排班
INSERT INTO `schedule` (doctor_id, work_date, time_slot, max_appointments, booked_count) VALUES
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '上午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '下午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '上午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '下午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '上午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '下午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '上午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '下午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 5 DAY), '上午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 5 DAY), '下午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 6 DAY), '上午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 6 DAY), '下午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 7 DAY), '上午', 20, 0),
(1, DATE_ADD(CURDATE(), INTERVAL 7 DAY), '下午', 20, 0);

-- 为第二位医生（王芳）创建排班
INSERT INTO `schedule` (doctor_id, work_date, time_slot, max_appointments, booked_count) VALUES
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '上午', 20, 0),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '下午', 20, 0),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '上午', 20, 0),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '下午', 20, 0),
(2, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '上午', 20, 0),
(2, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '下午', 20, 0);

-- 为第三位医生（张强）创建排班
INSERT INTO `schedule` (doctor_id, work_date, time_slot, max_appointments, booked_count) VALUES
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '上午', 20, 0),
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '下午', 20, 0),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '上午', 20, 0),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '下午', 20, 0);

-- 5. 系统公告
INSERT INTO `announcement` (title, content, type, status, publish_time) VALUES
('欢迎使用医疗问诊管理系统', '本系统提供在线问诊、预约挂号、电子病历等功能，为您的健康保驾护航。', 'notice', 1, NOW()),
('春季流感预防知识', '春季是流感高发季节，请注意个人卫生，勤洗手，多通风，必要时接种流感疫苗。', 'health', 1, NOW()),
('系统维护通知', '本系统将于本周日凌晨2:00-4:00进行系统维护，届时将无法访问，请提前安排就诊时间。', 'notice', 1, NOW());
