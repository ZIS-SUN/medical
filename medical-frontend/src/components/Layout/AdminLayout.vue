<template>
  <el-container class="admin-layout">
    <el-aside width="200px">
      <div class="logo">
        <h2>管理后台</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="transparent"
        text-color="#CCCCCC"
        active-text-color="#FF6B00"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据看板</span>
        </el-menu-item>
        <el-menu-item index="/admin/doctors">
          <el-icon><User /></el-icon>
          <span>医生管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/schedules">
          <el-icon><Calendar /></el-icon>
          <span>排班管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/appointments">
          <el-icon><Document /></el-icon>
          <span>预约管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/consultations">
          <el-icon><ChatDotRound /></el-icon>
          <span>问诊管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/records">
          <el-icon><Notebook /></el-icon>
          <span>病历管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/announcements">
          <el-icon><Bell /></el-icon>
          <span>公告管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><UserFilled /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-content">
          <span>医疗问诊管理系统</span>
          <div class="user-info">
            <span class="username">{{ adminName }}</span>
            <el-button @click="handleLogout" type="danger" size="small">退出登录</el-button>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAdminStore } from '@/stores/admin'
import { ElMessage } from 'element-plus'
import {
  DataAnalysis,
  User,
  Calendar,
  Document,
  ChatDotRound,
  Notebook,
  Bell,
  UserFilled
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const adminStore = useAdminStore()

const activeMenu = computed(() => route.path)
const adminName = computed(() => adminStore.adminName)

const handleLogout = () => {
  adminStore.logout()
  ElMessage.success('退出成功')
  router.push('/admin/login')
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  background-color: var(--bg-color);
}

.el-aside {
  background: linear-gradient(180deg, #001529 0%, #002140 100%);
  color: #fff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  transition: var(--transition-base);
}

.logo {
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-gradient);
  box-shadow: 0 2px 12px rgba(0, 180, 166, 0.3);
  position: relative;
  overflow: hidden;
}

.logo::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0) 100%);
}

.logo h2 {
  color: #fff;
  font-size: var(--font-size-lg);
  font-weight: 700;
  position: relative;
  z-index: 1;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.el-menu {
  border: none;
  padding: var(--spacing-md) 0;
}

:deep(.el-menu-item) {
  margin: 4px 12px;
  border-radius: var(--border-radius);
  transition: var(--transition-base);
  font-size: var(--font-size-sm);
}

:deep(.el-menu-item:hover) {
  background-color: rgba(0, 180, 166, 0.1) !important;
}

:deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(0, 180, 166, 0.2) 0%, rgba(0, 206, 201, 0.1) 100%) !important;
  border-left: 3px solid var(--primary-color);
  font-weight: 600;
}

:deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 8px;
}

.el-header {
  background-color: var(--bg-white);
  box-shadow: var(--box-shadow-sm);
  padding: 0 var(--spacing-xl);
  border-bottom: 1px solid var(--border-light);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.header-content > span {
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--text-primary);
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.username {
  color: var(--text-primary);
  font-weight: 500;
  font-size: var(--font-size-sm);
}

.user-info :deep(.el-button) {
  border-radius: var(--border-radius);
  font-weight: 500;
}

.el-main {
  background-color: var(--bg-color);
  padding: var(--spacing-lg);
  overflow-y: auto;
}

/* 滚动条样式 */
.el-main::-webkit-scrollbar {
  width: 8px;
}

.el-main::-webkit-scrollbar-track {
  background: var(--bg-light);
  border-radius: 4px;
}

.el-main::-webkit-scrollbar-thumb {
  background: var(--border-dark);
  border-radius: 4px;
  transition: var(--transition-base);
}

.el-main::-webkit-scrollbar-thumb:hover {
  background: var(--primary-color);
}
</style>

