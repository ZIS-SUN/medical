<template>
  <el-card class="doctor-card" shadow="hover" @click="handleClick">
    <div class="card-content">
      <el-avatar :size="80" :src="doctor.avatar">{{ doctor.name }}</el-avatar>
      <div class="doctor-info">
        <h3 class="doctor-name">{{ doctor.name }}</h3>
        <p class="doctor-title">{{ doctor.title }}</p>
        <p class="doctor-department">{{ doctor.departmentName }}</p>
        <div class="doctor-rating">
          <el-rate 
            v-model="doctor.avgRating" 
            disabled 
            :allow-half="true"
            :size="14"
          />
          <span class="rating-count">({{ doctor.reviewCount || 0 }}条评价)</span>
        </div>
      </div>
    </div>
    <el-divider />
    <div class="card-footer">
      <el-button type="primary" size="small" @click.stop="handleAppointment">
        预约挂号
      </el-button>
      <el-button size="small" @click.stop="handleViewDetail">
        查看详情
      </el-button>
    </div>
  </el-card>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  doctor: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['appointment'])
const router = useRouter()

const handleClick = () => {
  router.push(`/doctors/${props.doctor.id}`)
}

const handleViewDetail = () => {
  router.push(`/doctors/${props.doctor.id}`)
}

const handleAppointment = () => {
  emit('appointment', props.doctor)
  router.push(`/doctors/${props.doctor.id}`)
}
</script>

<style scoped>
.doctor-card {
  cursor: pointer;
  transition: var(--transition-base);
  border-radius: var(--border-radius-xl);
  border: 2px solid transparent;
  overflow: hidden;
  background: var(--bg-white);
}

.doctor-card :deep(.el-card__body) {
  padding: var(--spacing-xl);
}

.doctor-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--box-shadow-hover);
  border-color: var(--primary-light);
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-md);
  position: relative;
}

.card-content :deep(.el-avatar) {
  border: 3px solid var(--border-light);
  transition: var(--transition-base);
  box-shadow: var(--box-shadow-sm);
}

.doctor-card:hover .card-content :deep(.el-avatar) {
  border-color: var(--primary-color);
  transform: scale(1.05);
  box-shadow: var(--box-shadow);
}

.doctor-info {
  text-align: center;
  width: 100%;
}

.doctor-name {
  font-size: var(--font-size-lg);
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--spacing-sm);
  transition: var(--transition-fast);
}

.doctor-card:hover .doctor-name {
  color: var(--primary-color);
}

.doctor-title {
  font-size: var(--font-size-sm);
  font-weight: 600;
  color: var(--primary-color);
  margin-bottom: var(--spacing-xs);
  padding: 4px 12px;
  background: var(--primary-lighter);
  display: inline-block;
  border-radius: var(--border-radius-lg);
}

.doctor-department {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin-bottom: var(--spacing-sm);
  font-weight: 500;
}

.doctor-rating {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) 0;
}

.rating-count {
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
}

.card-footer {
  display: flex;
  justify-content: center;
  gap: var(--spacing-md);
  margin-top: var(--spacing-sm);
}

.card-footer :deep(.el-button) {
  border-radius: var(--border-radius);
  font-weight: 500;
  transition: var(--transition-base);
  flex: 1;
}

.card-footer :deep(.el-button--primary) {
  background: var(--primary-gradient);
  border: none;
  box-shadow: 0 2px 8px rgba(0, 180, 166, 0.3);
}

.card-footer :deep(.el-button--primary:hover) {
  box-shadow: 0 4px 12px rgba(0, 180, 166, 0.4);
  transform: translateY(-1px);
}

.card-footer :deep(.el-button:not(.el-button--primary)) {
  border-color: var(--border-color);
}

.card-footer :deep(.el-button:not(.el-button--primary):hover) {
  border-color: var(--primary-color);
  color: var(--primary-color);
  background-color: var(--primary-lighter);
}

/* 分隔线优化 */
:deep(.el-divider) {
  margin: var(--spacing-md) 0;
  border-color: var(--border-light);
}
</style>

