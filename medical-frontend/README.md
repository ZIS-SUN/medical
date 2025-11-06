# 医疗问诊管理系统 - 前端

基于 Vue 3 + Element Plus + Pinia + Axios 的医疗问诊管理系统前端项目。

## 技术栈

- **Vue 3.3+** - 渐进式 JavaScript 框架
- **Vite 5.x** - 下一代前端构建工具
- **Element Plus 2.4+** - Vue 3 组件库
- **Pinia 2.x** - Vue 状态管理
- **Vue Router 4.x** - Vue 路由管理
- **Axios 1.6+** - HTTP 客户端
- **ECharts 5.4+** - 数据可视化
- **Day.js** - 日期处理库

## 项目结构

```
medical-frontend/
├── public/              # 静态资源
├── src/
│   ├── api/            # API 接口
│   ├── assets/         # 资源文件
│   ├── components/     # 公共组件
│   ├── router/         # 路由配置
│   ├── stores/         # 状态管理
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   └── main.js         # 入口文件
├── .env.development    # 开发环境配置
├── .env.production     # 生产环境配置
├── vite.config.js      # Vite 配置
└── package.json        # 依赖配置
```

## 开发指南

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:3000

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 功能模块

### 患者端
- 首页（轮播图、科室导航、热门医生）
- 医生列表与搜索
- 医生详情与预约
- 在线问诊
- 我的预约
- 我的问诊
- 电子病历
- 个人中心
- 医生评价

### 管理端
- 管理员登录
- 数据看板
- 医生管理
- 排班管理
- 预约管理
- 问诊管理
- 病历管理
- 公告管理
- 用户管理

## 开发规范

- 使用 Vue 3 Composition API
- 遵循 ESLint 代码规范
- 组件命名使用 PascalCase
- 文件命名使用 kebab-case
- 提交代码前格式化代码

## 注意事项

1. 确保后端服务已启动（默认端口 8080）
2. 开发环境会自动代理 `/api` 请求到后端
3. 图片上传最大 5MB
4. 需要登录的页面会自动跳转登录页

## License

MIT




