<template>
  <div class="home-page">
    <!-- 轮播图 -->
    <el-carousel height="400px" class="banner">
      <el-carousel-item v-for="item in banners" :key="item.id">
        <div class="banner-item" :style="{ backgroundColor: item.color }">
          <h2>{{ item.title }}</h2>
          <p>{{ item.description }}</p>
        </div>
      </el-carousel-item>
    </el-carousel>

    <div class="home-container">
      <!-- 科室导航 -->
      <section class="section">
        <h2 class="section-title">科室导航</h2>
        <el-row :gutter="20" class="department-grid">
          <el-col :span="6" v-for="dept in departments" :key="dept.id">
            <el-card class="department-card" shadow="hover" @click="gotoDepartment(dept)">
              <div class="department-content">
                <el-icon :size="40" :color="dept.color || '#409EFF'">
                  <component :is="dept.icon || 'Document'" />
                </el-icon>
                <p class="department-name">{{ dept.name }}</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </section>

      <!-- 热门医生 -->
      <section class="section">
        <div class="section-header">
          <h2 class="section-title">热门医生</h2>
          <router-link to="/doctors" class="more-link">
            查看更多 <el-icon><ArrowRight /></el-icon>
          </router-link>
        </div>
        <el-row :gutter="20">
          <el-col :span="6" v-for="doctor in hotDoctors" :key="doctor.id">
            <DoctorCard :doctor="doctor" />
          </el-col>
        </el-row>
      </section>

      <!-- 系统公告 -->
      <section class="section">
        <h2 class="section-title">系统公告</h2>
        <el-card>
          <el-timeline>
            <el-timeline-item
              v-for="item in announcements"
              :key="item.id"
              :timestamp="item.publishTime"
              placement="top"
            >
              <h4 class="announcement-title">{{ item.title }}</h4>
              <p class="announcement-content">{{ item.content }}</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getDepartments } from '@/api/department'
import { getHotDoctors } from '@/api/doctor'
import { getAnnouncements } from '@/api/announcement'
import DoctorCard from '@/components/DoctorCard.vue'
import { ArrowRight, Document } from '@element-plus/icons-vue'

const router = useRouter()

const banners = ref([
  {
    id: 1,
    title: '欢迎使用医疗问诊管理系统',
    description: '在线预约挂号，方便快捷',
    color: '#FF6B00'
  },
  {
    id: 2,
    title: '专业的医疗团队',
    description: '经验丰富的医生为您服务',
    color: '#52C41A'
  },
  {
    id: 3,
    title: '在线问诊服务',
    description: '足不出户，在线咨询医生',
    color: '#FAAD14'
  }
])

const departments = ref([])
const hotDoctors = ref([])
const announcements = ref([])

const gotoDepartment = (dept) => {
  router.push({ path: '/doctors', query: { departmentId: dept.id } })
}

onMounted(async () => {
  try {
    const [deptData, doctorData, announcementData] = await Promise.all([
      getDepartments(),
      getHotDoctors(),
      getAnnouncements({ page: 1, pageSize: 5 })
    ])
    
    departments.value = deptData
    hotDoctors.value = doctorData
    announcements.value = announcementData.list || announcementData
  } catch (error) {
    console.error('加载数据失败:', error)
  }
})
</script>

<style scoped>
.home-page {
  background-color: var(--bg-color);
}

.banner {
  margin-bottom: var(--spacing-xxl);
  border-radius: 0 0 var(--border-radius-xxl) var(--border-radius-xxl);
  overflow: hidden;
  box-shadow: var(--box-shadow-lg);
}

.banner-item {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;
  position: relative;
  overflow: hidden;
}

.banner-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(0, 0, 0, 0.1) 0%, rgba(0, 0, 0, 0.3) 100%);
}

.banner-item h2 {
  font-size: var(--font-size-huge);
  font-weight: 700;
  margin-bottom: var(--spacing-lg);
  position: relative;
  z-index: 1;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  animation: fadeInUp 0.8s ease-out;
}

.banner-item p {
  font-size: var(--font-size-lg);
  position: relative;
  z-index: 1;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
  animation: fadeInUp 1s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.home-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 var(--spacing-xl) var(--spacing-xxl);
}

.section {
  margin-bottom: var(--spacing-xxl);
  animation: fadeIn 0.6s ease-out;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.section-title {
  font-size: var(--font-size-xxl);
  color: var(--text-primary);
  font-weight: 700;
  margin-bottom: var(--spacing-lg);
  position: relative;
  padding-left: var(--spacing-md);
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 5px;
  height: 28px;
  background: var(--primary-gradient);
  border-radius: 3px;
}

.more-link {
  color: var(--primary-color);
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  font-size: var(--font-size-sm);
  font-weight: 500;
  transition: var(--transition-fast);
  padding: 8px 16px;
  border-radius: var(--border-radius-lg);
}

.more-link:hover {
  background-color: var(--primary-lighter);
  transform: translateX(4px);
}

.department-grid {
  margin-bottom: 0;
}

.department-card {
  cursor: pointer;
  transition: var(--transition-base);
  border-radius: var(--border-radius-lg);
  border: 2px solid transparent;
  background: var(--bg-white);
  overflow: hidden;
}

.department-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--box-shadow-hover);
  border-color: var(--primary-light);
}

.department-card :deep(.el-card__body) {
  padding: var(--spacing-lg);
}

.department-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-lg) 0;
}

.department-content :deep(.el-icon) {
  transition: var(--transition-base);
}

.department-card:hover .department-content :deep(.el-icon) {
  transform: scale(1.1);
}

.department-name {
  font-size: var(--font-size-base);
  font-weight: 600;
  color: var(--text-primary);
}

/* 公告样式优化 */
.section :deep(.el-card) {
  border-radius: var(--border-radius-lg);
  box-shadow: var(--box-shadow-sm);
  border: 1px solid var(--border-light);
  transition: var(--transition-base);
}

.section :deep(.el-card:hover) {
  box-shadow: var(--box-shadow);
}

.section :deep(.el-timeline-item__wrapper) {
  padding-left: var(--spacing-lg);
}

.announcement-title {
  color: var(--text-primary);
  margin-bottom: var(--spacing-sm);
  font-weight: 600;
  font-size: var(--font-size-base);
}

.announcement-content {
  color: var(--text-secondary);
  font-size: var(--font-size-sm);
  line-height: var(--line-height-relaxed);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner-item h2 {
    font-size: var(--font-size-xxl);
  }

  .banner-item p {
    font-size: var(--font-size-base);
  }

  .home-container {
    padding: 0 var(--spacing-md) var(--spacing-xl);
  }
}
</style>

