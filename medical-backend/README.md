# 医疗问诊管理系统 - 后端

基于Spring Boot的医疗问诊管理系统后端服务

## 技术栈

- Spring Boot 2.7.18
- MyBatis Plus 3.5.5
- MySQL 8.0+
- Redis 6.0+（可选）
- JWT认证
- Knife4j接口文档

## 项目结构

```
medical-backend/
├── src/main/java/com/medical/
│   ├── MedicalApplication.java    # 启动类
│   ├── common/                    # 公共类
│   │   ├── Result.java            # 统一响应
│   │   ├── BusinessException.java # 业务异常
│   │   └── PageResult.java        # 分页响应
│   ├── config/                    # 配置类
│   │   ├── CorsConfig.java        # 跨域配置
│   │   └── Knife4jConfig.java     # 接口文档配置
│   ├── controller/                # 控制器
│   │   └── AuthController.java    # 认证控制器
│   ├── dto/                       # 数据传输对象
│   │   ├── request/               # 请求参数
│   │   └── response/              # 响应对象
│   ├── entity/                    # 实体类
│   ├── handler/                   # 处理器
│   │   └── GlobalExceptionHandler.java # 全局异常处理
│   ├── mapper/                    # 数据访问层
│   ├── service/                   # 业务逻辑层
│   │   └── AuthService.java       # 认证服务
│   └── util/                      # 工具类
│       ├── JwtUtil.java           # JWT工具
│       ├── PasswordUtil.java      # 密码加密
│       └── FileUtil.java          # 文件上传
├── src/main/resources/
│   ├── application.yml            # 配置文件
│   └── mapper/                    # MyBatis XML
├── init.sql                       # 数据库初始化脚本
├── pom.xml                        # Maven依赖
└── README.md                      # 本文档
```

## 快速开始

### 1. 环境准备

确保已安装：
- JDK 1.8 或更高版本
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+（可选）

### 2. 创建数据库

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source /path/to/init.sql

# 或者直接导入
mysql -u root -p < init.sql
```

初始化脚本会创建：
- 数据库：`medical_db`
- 10张表：user、doctor、department、appointment、consultation、medical_record、review、admin、announcement、schedule
- 测试数据：科室、医生、管理员、排班、公告

### 3. 修改配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medical_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password  # 修改为你的MySQL密码

file:
  upload:
    path: /Users/zishen/Desktop/medical-uploads  # 修改为你的文件上传路径
```

### 4. 创建上传目录

```bash
mkdir -p /Users/zishen/Desktop/medical-uploads
```

### 5. 启动项目

**方式一：使用Maven**
```bash
cd medical-backend
mvn spring-boot:run
```

**方式二：使用IDE**
- 使用IntelliJ IDEA打开项目
- 运行 `MedicalApplication` 启动类

### 6. 访问接口文档

启动成功后，访问：

```
http://localhost:8080/doc.html
```

## 默认账号

### 管理员账号
- 用户名：`admin`
- 密码：`admin123`

### 测试数据
- 科室：9个（内科、外科、儿科等）
- 医生：7位
- 公告：3条
- 排班：已为前3位医生设置未来7天排班

## API接口

### 已实现接口

#### 患者端接口

**认证模块：**
- ✅ `POST /api/auth/register` - 用户注册
- ✅ `POST /api/auth/login` - 用户登录

**科室模块：**
- ✅ `GET /api/departments` - 获取科室树形列表
- ✅ `GET /api/departments/all` - 获取所有科室（平铺）

**医生模块：**
- ✅ `GET /api/doctors` - 分页查询医生列表（支持科室、职称、关键词筛选）
- ✅ `GET /api/doctors/{id}` - 获取医生详情

**预约模块：**
- ✅ `POST /api/appointments` - 提交预约（需要认证）
- ✅ `GET /api/appointments/my` - 查询我的预约（需要认证）
- ✅ `PUT /api/appointments/{id}/cancel` - 取消预约（需要认证）

**公告模块：**
- ✅ `GET /api/announcements` - 获取公告列表

**文件上传：**
- ✅ `POST /api/upload` - 文件上传（需要认证）

#### 管理端接口

**认证模块：**
- ✅ `POST /admin/auth/login` - 管理员登录

### 待实现接口（可选）

根据 `backend-prd.md` 文档，还需实现以下接口：

**患者端：**
- 科室管理：`GET /api/departments`
- 医生管理：`GET /api/doctors`、`GET /api/doctors/{id}`
- 预约管理：`POST /api/appointments`、`GET /api/appointments/my`、`PUT /api/appointments/{id}/cancel`
- 问诊管理：`POST /api/consultations`、`GET /api/consultations/my`
- 病历管理：`GET /api/medical-records/my`
- 评价管理：`POST /api/reviews`、`GET /api/doctors/{id}/reviews`
- 公告管理：`GET /api/announcements`
- 文件上传：`POST /api/upload`

**管理端：**
- 认证：`POST /admin/auth/login`
- 数据看板：`GET /admin/dashboard/stats`
- 医生管理：`GET/POST/PUT/DELETE /admin/doctors`
- 预约管理：`GET /admin/appointments`、`PUT /admin/appointments/{id}/status`
- 问诊管理：`GET /admin/consultations`、`POST /admin/consultations/{id}/reply`
- 排班管理：`GET/POST /admin/schedules`
- 公告管理：`GET/POST/PUT/DELETE /admin/announcements`
- 用户管理：`GET /admin/users`、`PUT /admin/users/{id}/status`

## 开发说明

### 添加新接口

1. **创建DTO类**（`dto/request`、`dto/response`）
2. **创建Service**（`service`）
3. **创建Controller**（`controller`）
4. **测试接口**（使用Knife4j或Postman）

### 统一响应格式

```json
{
  "code": 0,
  "msg": "success",
  "data": {}
}
```

### 错误码规范

| 错误码 | 说明 |
|---|---|
| 0 | 成功 |
| 400 | 参数错误 |
| 401 | 未登录 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 409 | 业务冲突 |
| 500 | 服务器错误 |
| 10001-10999 | 用户相关错误 |
| 20001-20999 | 预约相关错误 |
| 30001-30999 | 评价相关错误 |

### JWT认证

所有需要认证的接口，请求头需携带Token：
```
Authorization: Bearer {token}
```

## 测试

### 测试注册

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "realName": "测试用户",
    "phone": "13800138000",
    "email": "test@example.com",
    "gender": 1
  }'
```

### 测试登录

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

## 常见问题

### 1. 启动失败：连接数据库失败

- 检查MySQL是否启动
- 检查数据库名、用户名、密码是否正确
- 检查是否已执行init.sql创建数据库

### 2. 接口返回401

- 检查是否携带Token
- 检查Token是否过期（默认7天）

### 3. 密码加密问题

- 系统使用BCrypt加密，强度为10
- 可使用`PasswordUtil.main()`方法生成加密密码

### 4. 文件上传失败

- 检查上传目录是否存在
- 检查目录权限
- 检查文件大小（限制5MB）
- 检查文件格式（只支持jpg/png）

## 部署说明

### 打包

```bash
mvn clean package
```

生成的jar包位于 `target/medical-backend-1.0.0.jar`

### 运行

```bash
java -jar target/medical-backend-1.0.0.jar
```

### 指定配置文件

```bash
java -jar target/medical-backend-1.0.0.jar --spring.profiles.active=prod
```

## 开发路线图

- [x] 项目基础架构搭建
- [x] 数据库设计与初始化
- [x] 用户认证（注册/登录）
- [ ] 科室与医生管理
- [ ] 预约挂号功能
- [ ] 在线问诊功能
- [ ] 病历管理
- [ ] 评价功能
- [ ] 管理端功能
- [ ] 单元测试
- [ ] 接口文档完善

## 技术支持

如有问题，请参考：
- backend-prd.md - 详细需求文档
- Knife4j文档 - http://localhost:8080/doc.html
- Spring Boot官方文档 - https://spring.io/projects/spring-boot

## License

Copyright © 2025 Medical System. All rights reserved.
