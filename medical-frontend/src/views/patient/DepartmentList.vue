<template>
  <div class="department-list-page">
    <div class="page-container">
      <h1 class="page-title">科室列表</h1>

      <div v-loading="loading" class="department-list">
        <el-row :gutter="20">
          <el-col :span="6" v-for="dept in departments" :key="dept.id">
            <el-card class="department-card" shadow="hover" @click="handleClick(dept)">
              <div class="department-content">
                <el-icon :size="60" :color="dept.color || '#409EFF'">
                  <component :is="dept.icon || 'Document'" />
                </el-icon>
                <h3 class="department-name">{{ dept.name }}</h3>
                <p class="department-description">{{ dept.description }}</p>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-empty v-if="!loading && departments.length === 0" description="暂无科室数据" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getDepartments } from '@/api/department'
import { Document } from '@element-plus/icons-vue'

const router = useRouter()
const departments = ref([])
const loading = ref(false)

const handleClick = (dept) => {
  router.push({ path: '/doctors', query: { departmentId: dept.id } })
}

onMounted(async () => {
  loading.value = true
  try {
    departments.value = await getDepartments()
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.department-list {
  min-height: 400px;
}

.department-card {
  cursor: pointer;
  transition: all 0.3s;
  height: 100%;
}

.department-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.department-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 30px 20px;
  text-align: center;
}

.department-name {
  font-size: 20px;
  font-weight: bold;
  color: var(--text-primary);
  margin: 0;
}

.department-description {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 0;
}
</style>




