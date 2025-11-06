<template>
  <header class="header">
    <div class="header-container">
      <div class="logo" @click="goHome">
        <h1>医疗问诊管理系统</h1>
      </div>
      <nav class="nav">
        <router-link to="/" class="nav-item">首页</router-link>
        <router-link to="/departments" class="nav-item">科室</router-link>
        <router-link to="/doctors" class="nav-item">医生</router-link>
        <router-link to="/consultation" class="nav-item">在线问诊</router-link>
      </nav>
      <div class="user-section">
        <template v-if="isLoggedIn">
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              <el-avatar :size="32" :src="avatar">{{ username.charAt(0) }}</el-avatar>
              <span class="username">{{ username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="appointments">我的预约</el-dropdown-item>
                <el-dropdown-item command="consultations">我的问诊</el-dropdown-item>
                <el-dropdown-item command="records">电子病历</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button @click="goLogin">登录</el-button>
          <el-button type="primary" @click="goRegister">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const isLoggedIn = computed(() => userStore.isLoggedIn)
const username = computed(() => userStore.username)
const avatar = computed(() => userStore.avatar)

const goHome = () => {
  router.push('/')
}

const goLogin = () => {
  router.push('/login')
}

const goRegister = () => {
  router.push('/register')
}

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    ElMessage.success('退出成功')
    router.push('/')
  } else if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'appointments') {
    router.push('/my-appointments')
  } else if (command === 'consultations') {
    router.push('/my-consultations')
  } else if (command === 'records') {
    router.push('/my-records')
  }
}
</script>

<style scoped>
.header {
  background-color: var(--bg-white);
  box-shadow: var(--box-shadow-sm);
  height: 70px;
  position: sticky;
  top: 0;
  z-index: 100;
  transition: var(--transition-base);
  backdrop-filter: blur(8px);
  background: rgba(255, 255, 255, 0.95);
}

.header-container {
  max-width: 1400px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--spacing-xl);
}

.logo {
  cursor: pointer;
  transition: var(--transition-fast);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.logo:hover {
  transform: scale(1.02);
}

.logo h1 {
  font-size: var(--font-size-xl);
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.nav {
  display: flex;
  gap: 40px;
  align-items: center;
}

.nav-item {
  color: var(--text-regular);
  font-size: var(--font-size-base);
  font-weight: 500;
  transition: var(--transition-fast);
  position: relative;
  padding: 8px 4px;
}

.nav-item::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%) scaleX(0);
  width: 100%;
  height: 3px;
  background: var(--primary-gradient);
  border-radius: 2px;
  transition: var(--transition-base);
}

.nav-item:hover {
  color: var(--primary-color);
}

.nav-item:hover::after,
.nav-item.router-link-active::after {
  transform: translateX(-50%) scaleX(1);
}

.nav-item.router-link-active {
  color: var(--primary-color);
  font-weight: 600;
}

.user-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  cursor: pointer;
  padding: 8px 16px;
  border-radius: var(--border-radius-lg);
  transition: var(--transition-fast);
}

.user-dropdown:hover {
  background-color: var(--bg-hover);
}

.username {
  color: var(--text-primary);
  font-size: var(--font-size-sm);
  font-weight: 500;
}

/* 登录注册按钮样式优化 */
.user-section :deep(.el-button) {
  border-radius: var(--border-radius-lg);
  font-weight: 500;
  transition: var(--transition-base);
}

.user-section :deep(.el-button--primary) {
  background: var(--primary-gradient);
  border: none;
  box-shadow: 0 2px 8px rgba(0, 180, 166, 0.3);
}

.user-section :deep(.el-button--primary:hover) {
  box-shadow: 0 4px 12px rgba(0, 180, 166, 0.4);
  transform: translateY(-1px);
}
</style>

