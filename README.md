# 医疗问诊管理系统

一个功能完善的医疗问诊管理系统，支持患者在线预约、问诊、查看病历，医生管理排班、回复问诊，以及管理员进行系统管理和数据统计。

## 项目结构

```
medical/
├── medical-backend/          # 后端项目（Spring Boot）
│   ├── src/                 # 源代码
│   ├── init.sql            # 数据库结构初始化脚本
│   ├── test-data.sql       # 测试数据初始化脚本
│   ├── pom.xml             # Maven配置文件
│   └── README.md           # 后端说明文档
├── medical-frontend/         # 前端项目（Vue 3）
│   ├── src/                # 源代码
│   ├── package.json        # npm配置文件
│   └── README.md           # 前端说明文档
├── test-accounts.md         # 测试账号信息
└── README.md               # 项目总说明文档
```

## 技术栈

### 后端
- Java 17
- Spring Boot 3.x
- MyBatis-Plus
- MySQL 8.0+
- Redis
- JWT认证
- Knife4j API文档

### 前端
- Vue 3
- Vite
- Vue Router
- Pinia
- Axios
- Element Plus

## 功能特性

### 患者端
- ✅ 用户注册登录
- ✅ 浏览科室和医生信息
- ✅ 在线预约挂号
- ✅ 在线问诊咨询
- ✅ 查看个人病历
- ✅ 医生评价

### 医生端
- ✅ 医生登录
- ✅ 查看排班信息
- ✅ 回复患者问诊
- ✅ 创建和管理病历
- ✅ 查看患者评价

### 管理端
- ✅ 管理员登录
- ✅ 数据统计看板
- ✅ 用户管理
- ✅ 医生管理
- ✅ 科室管理
- ✅ 排班管理
- ✅ 预约管理
- ✅ 问诊管理
- ✅ 病历管理
- ✅ 评价管理
- ✅ 公告管理
- ✅ 数据导出

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 5.0+
- Maven 3.6+

### 数据库初始化

1. 创建数据库：
```sql
CREATE DATABASE medical_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 导入数据库结构：
```bash
mysql -u root -p medical_system < medical-backend/init.sql
```

3. 导入测试数据：
```bash
mysql -u root -p medical_system < medical-backend/test-data.sql
```

### 后端启动

1. 进入后端目录：
```bash
cd medical-backend
```

2. 修改配置文件 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medical_system
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
```

3. 启动后端：
```bash
mvn clean install
mvn spring-boot:run
```

4. 访问API文档：
```
http://localhost:8080/doc.html
```

### 前端启动

1. 进入前端目录：
```bash
cd medical-frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动前端：
```bash
npm run dev
```

4. 访问前端页面：
```
http://localhost:5173
```

## 测试账号

详见 [test-accounts.md](test-accounts.md)

**快速测试：**
- 管理员：admin / admin123
- 医生：doctor01 / admin123
- 患者：test001 / 123456

## 项目截图

（可根据需要添加项目截图）

## API文档

启动后端后，访问：http://localhost:8080/doc.html

## 开发说明

### 后端目录结构
```
src/main/java/com/medical/
├── common/          # 公共类（异常、结果封装等）
├── config/          # 配置类（跨域、Swagger等）
├── controller/      # 控制器层
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── mapper/         # MyBatis Mapper
├── service/        # 服务层
├── util/           # 工具类
├── handler/        # 全局异常处理
└── interceptor/    # 拦截器（JWT认证）
```

### 前端目录结构
```
src/
├── api/            # API接口封装
├── assets/         # 静态资源
├── components/     # 公共组件
├── router/         # 路由配置
├── stores/         # 状态管理
├── utils/          # 工具函数
└── views/          # 页面组件
    ├── admin/      # 管理端页面
    └── patient/    # 患者端页面
```

## 常见问题

**Q: 无法连接数据库？**  
A: 检查MySQL服务是否启动，配置文件中的数据库连接信息是否正确。

**Q: Redis连接失败？**  
A: 确保Redis服务已启动，默认端口为6379。

**Q: 前端请求跨域？**  
A: 后端已配置CORS，允许前端跨域访问。

**Q: JWT Token过期？**  
A: Token默认有效期24小时，过期后需重新登录。

## 许可证

MIT License

## 联系方式

如有问题或建议，欢迎提Issue。

