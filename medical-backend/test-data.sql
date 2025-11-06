-- =====================
-- 测试数据SQL
-- =====================

USE medical_db;

-- 清理旧测试数据（按照外键依赖顺序）
DELETE FROM `review` WHERE user_id >= 1 AND user_id <= 5;
DELETE FROM `medical_record` WHERE user_id >= 1 AND user_id <= 5;
DELETE FROM `consultation` WHERE user_id >= 1 AND user_id <= 5;
DELETE FROM `appointment` WHERE user_id >= 1 AND user_id <= 5;
DELETE FROM `user` WHERE username LIKE 'test%';

-- 1. 测试患者用户数据（密码：123456，BCrypt加密）
-- BCrypt加密密码 (使用 src/test/java/PasswordGenerator.java 生成)
-- 密码: 123456, 哈希: $2a$10$eDFUVgga3tk7ixVK.cPwmudMTKhAeUzIZmerZ3CaA9j8SQ00WozSW
INSERT INTO `user` (username, password, real_name, gender, phone, email, status) VALUES
('test001', '$2a$10$eDFUVgga3tk7ixVK.cPwmudMTKhAeUzIZmerZ3CaA9j8SQ00WozSW', '张三', 1, '13800138001', 'zhangsan@test.com', 1),
('test002', '$2a$10$eDFUVgga3tk7ixVK.cPwmudMTKhAeUzIZmerZ3CaA9j8SQ00WozSW', '李四', 2, '13800138002', 'lisi@test.com', 1),
('test003', '$2a$10$eDFUVgga3tk7ixVK.cPwmudMTKhAeUzIZmerZ3CaA9j8SQ00WozSW', '王五', 1, '13800138003', 'wangwu@test.com', 1),
('test004', '$2a$10$eDFUVgga3tk7ixVK.cPwmudMTKhAeUzIZmerZ3CaA9j8SQ00WozSW', '赵六', 2, '13800138004', 'zhaoliu@test.com', 1),
('test005', '$2a$10$eDFUVgga3tk7ixVK.cPwmudMTKhAeUzIZmerZ3CaA9j8SQ00WozSW', '钱七', 1, '13800138005', 'qianqi@test.com', 1);

-- 2. 预约数据
INSERT INTO `appointment` (appointment_no, user_id, doctor_id, department_id, appointment_date, time_slot, status, remark, cancel_reason) VALUES
('APT20251106001', 1, 1, 11, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '上午', '已确认', '头晕，血压偏高', NULL),
('APT20251106002', 2, 2, 12, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '下午', '已确认', '胃痛，消化不良', NULL),
('APT20251106003', 3, 3, 21, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '上午', '待确认', '膝关节疼痛', NULL),
('APT20251106004', 4, 1, 11, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '下午', '待确认', '心悸，胸闷', NULL),
('APT20251106005', 5, 4, 13, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '上午', '已确认', '咳嗽，气喘', NULL),
('APT20251106006', 1, 2, 12, DATE_ADD(CURDATE(), INTERVAL -1 DAY), '上午', '已完成', '胃部不适', NULL),
('APT20251106007', 2, 3, 21, DATE_ADD(CURDATE(), INTERVAL -2 DAY), '下午', '已完成', '腰椎疼痛', NULL),
('APT20251106008', 3, 1, 11, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '下午', '已取消', '临时有事', '时间冲突');

-- 更新排班的已预约数
UPDATE `schedule` SET booked_count = booked_count + 1
WHERE doctor_id = 1 AND work_date = DATE_ADD(CURDATE(), INTERVAL 1 DAY) AND time_slot = '上午';

UPDATE `schedule` SET booked_count = booked_count + 1
WHERE doctor_id = 2 AND work_date = DATE_ADD(CURDATE(), INTERVAL 1 DAY) AND time_slot = '下午';

UPDATE `schedule` SET booked_count = booked_count + 1
WHERE doctor_id = 3 AND work_date = DATE_ADD(CURDATE(), INTERVAL 2 DAY) AND time_slot = '上午';

UPDATE `schedule` SET booked_count = booked_count + 1
WHERE doctor_id = 1 AND work_date = DATE_ADD(CURDATE(), INTERVAL 2 DAY) AND time_slot = '下午';

UPDATE `schedule` SET booked_count = booked_count + 1
WHERE doctor_id = 4 AND work_date = DATE_ADD(CURDATE(), INTERVAL 3 DAY) AND time_slot = '上午';

-- 3. 问诊数据
INSERT INTO `consultation` (consultation_no, user_id, doctor_id, question, status, reply_time, answer) VALUES
('CON20251106001', 1, 1, '医生您好，我最近总是头晕，血压也高，请问该怎么办？', '已回复', NOW(), '您好，根据您的描述，建议您定期监测血压，注意休息，饮食清淡，避免高盐高脂食物。如果症状持续，建议来院就诊做详细检查。'),
('CON20251106002', 2, 2, '胃痛好几天了，吃东西就不舒服，该吃什么药？', '已回复', NOW(), '建议您暂时避免辛辣、生冷食物，可以服用胃黏膜保护剂。如果疼痛加重或伴有呕吐，请及时就医。'),
('CON20251106003', 3, 3, '膝盖走路时很痛，是不是关节炎？', '待回复', NULL, NULL),
('CON20251106004', 4, 1, '胸闷心悸，是心脏有问题吗？需要做什么检查？', '待回复', NULL, NULL),
('CON20251106005', 5, 4, '咳嗽一个月了，吃了很多药都不好，该怎么办？', '已回复', NOW(), '长期咳嗽需要警惕，建议来院做胸部X光和肺功能检查，排除其他疾病。'),
('CON20251106006', 1, 2, '请问胃镜检查痛苦吗？需要注意什么？', '已回复', NOW(), '现在有无痛胃镜，检查过程中不会有太大不适。检查前需要空腹8小时，检查后2小时内不要进食。');

-- 4. 病历数据（针对已完成的预约）
INSERT INTO `medical_record` (record_no, appointment_id, user_id, doctor_id, chief_complaint, present_illness, diagnosis, prescription, advice) VALUES
('MR20251105001', 6, 1, 2, '胃部不适', '患者诉1周前开始出现胃部不适，餐后明显，伴有反酸、嗳气。无呕吐、腹泻。既往有慢性胃炎病史。', '慢性浅表性胃炎急性发作',
'1. 奥美拉唑肠溶片 20mg 每日2次 空腹服用\n2. 铝碳酸镁片 0.5g 每日3次 餐后服用\n3. 莫沙必利片 5mg 每日3次 餐前服用',
'1. 规律饮食，避免辛辣刺激食物\n2. 注意休息，避免过度劳累\n3. 戒烟限酒\n4. 2周后复诊\n5. 如症状加重，及时就医'),

('MR20251104001', 7, 2, 3, '腰椎疼痛', '患者诉3天前搬重物后出现腰痛，活动受限，以弯腰时明显。无下肢放射痛。', '急性腰扭伤',
'1. 双氯芬酸钠缓释片 75mg 每日1次 餐后服用\n2. 氯唑沙宗片 0.5g 每日3次 餐后服用\n3. 外用：云南白药气雾剂 局部喷敷 每日3-4次',
'1. 卧床休息2-3天\n2. 避免弯腰及负重\n3. 可局部热敷\n4. 1周后复诊\n5. 如出现下肢麻木无力，及时就医');

-- 5. 评价数据
INSERT INTO `review` (user_id, doctor_id, appointment_id, rating, content, status) VALUES
(1, 2, 6, 5, '李医生非常专业，态度和蔼，诊断准确，治疗效果很好，强烈推荐！', 1),
(2, 3, 7, 5, '张医生经验丰富，详细询问病情，给出了很好的治疗方案，已经好多了！', 1),
(1, 1, NULL, 5, '李明医生在线回复很及时，建议很有帮助，感谢！', 1),
(2, 2, NULL, 4, '王医生回复很专业，建议实用，但回复速度可以更快一些。', 1),
(5, 4, NULL, 5, '刘医生非常负责，问诊详细，给出的建议很有用！', 1);

-- 6. 添加更多排班数据（其他医生）
INSERT INTO `schedule` (doctor_id, work_date, time_slot, max_appointments, booked_count) VALUES
(4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '上午', 20, 0),
(4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '下午', 20, 0),
(4, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '上午', 20, 0),
(4, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '下午', 20, 0),
(4, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '上午', 20, 1),
(4, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '下午', 20, 0),
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '上午', 20, 0),
(5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '下午', 20, 0),
(6, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '上午', 20, 0),
(6, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '下午', 20, 0),
(7, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '上午', 20, 0),
(7, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '下午', 20, 0);

-- 完成测试数据导入
SELECT '测试数据导入完成！' AS message;
