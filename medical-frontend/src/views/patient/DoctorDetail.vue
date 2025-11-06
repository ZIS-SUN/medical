<template>
  <div class="doctor-detail-page">
    <div class="page-container">
      <el-card v-loading="loading" class="doctor-info-card">
        <div class="doctor-header">
          <el-avatar :size="120" :src="doctor.avatar">{{ doctor.name }}</el-avatar>
          <div class="doctor-info">
            <h2>{{ doctor.name }}</h2>
            <p class="title">{{ doctor.title }} | {{ doctor.departmentName }}</p>
            <div class="rating">
              <el-rate v-model="doctor.avgRating" disabled :allow-half="true" />
              <span class="rating-text">({{ doctor.reviewCount || 0 }}条评价)</span>
            </div>
          </div>
        </div>

        <el-divider />

        <div class="doctor-profile">
          <h3>医生简介</h3>
          <p>{{ doctor.profile || '暂无简介' }}</p>
        </div>
      </el-card>

      <!-- 排班信息 -->
      <el-card class="schedule-card">
        <h3>排班信息</h3>
        <div v-loading="scheduleLoading" class="schedule-list">
          <el-tag
            v-for="schedule in schedules"
            :key="schedule.id"
            :type="schedule.remaining > 0 ? 'success' : 'info'"
            :class="['schedule-tag', { selected: selectedSchedule?.id === schedule.id }]"
            @click="selectSchedule(schedule)"
          >
            {{ schedule.scheduleDate }} {{ schedule.timeSlot }}
            <span v-if="schedule.remaining > 0">(剩余{{ schedule.remaining }}个号)</span>
            <span v-else>(已满)</span>
          </el-tag>
          <el-empty v-if="schedules.length === 0" description="暂无排班信息" :image-size="80" />
        </div>
        <el-button
          type="primary"
          size="large"
          class="appointment-btn"
          :disabled="!selectedSchedule"
          @click="handleAppointment"
        >
          立即预约
        </el-button>
      </el-card>

      <!-- 患者评价 -->
      <el-card class="review-card">
        <h3>患者评价</h3>
        <div v-loading="reviewLoading" class="review-list">
          <div v-for="review in reviews" :key="review.id" class="review-item">
            <div class="review-header">
              <span class="user-name">{{ review.userName }}</span>
              <el-rate v-model="review.rating" disabled :size="16" />
            </div>
            <p class="review-content">{{ review.content }}</p>
            <span class="review-time">{{ review.createTime }}</span>
          </div>
          <el-empty v-if="reviews.length === 0" description="暂无评价" :image-size="80" />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getDoctorDetail, getDoctorSchedules } from '@/api/doctor'
import { getDoctorReviews } from '@/api/review'
import { createAppointment } from '@/api/appointment'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const doctor = ref({})
const schedules = ref([])
const reviews = ref([])
const selectedSchedule = ref(null)
const loading = ref(false)
const scheduleLoading = ref(false)
const reviewLoading = ref(false)

const fetchDoctorDetail = async () => {
  const doctorId = route.params.id
  loading.value = true
  try {
    doctor.value = await getDoctorDetail(doctorId)
  } finally {
    loading.value = false
  }
}

const fetchSchedules = async () => {
  const doctorId = route.params.id
  scheduleLoading.value = true
  try {
    schedules.value = await getDoctorSchedules(doctorId)
  } finally {
    scheduleLoading.value = false
  }
}

const fetchReviews = async () => {
  const doctorId = route.params.id
  reviewLoading.value = true
  try {
    const data = await getDoctorReviews(doctorId)
    reviews.value = data.list || data
  } finally {
    reviewLoading.value = false
  }
}

const selectSchedule = (schedule) => {
  if (schedule.remaining <= 0) {
    return ElMessage.warning('该时段已满')
  }
  selectedSchedule.value = schedule
}

const handleAppointment = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  if (!selectedSchedule.value) {
    return ElMessage.warning('请先选择预约时段')
  }

  try {
    await ElMessageBox.prompt('请输入备注信息（选填）', '确认预约', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      inputType: 'textarea'
    }).then(async ({ value }) => {
      await createAppointment({
        doctorId: doctor.value.id,
        scheduleId: selectedSchedule.value.id,
        appointmentDate: selectedSchedule.value.scheduleDate,
        timeSlot: selectedSchedule.value.timeSlot,
        remark: value || ''
      })
      
      ElMessage.success('预约成功')
      router.push('/my-appointments')
    })
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '预约失败')
    }
  }
}

onMounted(() => {
  fetchDoctorDetail()
  fetchSchedules()
  fetchReviews()
})
</script>

<style scoped>
.doctor-info-card {
  margin-bottom: 20px;
}

.doctor-header {
  display: flex;
  gap: 24px;
  align-items: center;
}

.doctor-info h2 {
  font-size: 24px;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.title {
  font-size: 16px;
  color: var(--text-secondary);
  margin-bottom: 12px;
}

.rating {
  display: flex;
  align-items: center;
  gap: 8px;
}

.rating-text {
  font-size: 14px;
  color: var(--text-secondary);
}

.doctor-profile h3 {
  margin-bottom: 16px;
  color: var(--text-primary);
}

.doctor-profile p {
  line-height: 1.8;
  color: var(--text-regular);
}

.schedule-card,
.review-card {
  margin-bottom: 20px;
}

.schedule-card h3,
.review-card h3 {
  margin-bottom: 20px;
  color: var(--text-primary);
}

.schedule-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 24px;
  min-height: 100px;
}

.schedule-tag {
  cursor: pointer;
  font-size: 14px;
  padding: 12px 16px;
}

.schedule-tag.selected {
  border: 2px solid var(--primary-color);
}

.appointment-btn {
  width: 100%;
}

.review-list {
  min-height: 200px;
}

.review-item {
  padding: 16px 0;
  border-bottom: 1px solid var(--border-light);
}

.review-item:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.user-name {
  font-weight: 500;
  color: var(--text-primary);
}

.review-content {
  color: var(--text-regular);
  line-height: 1.6;
  margin-bottom: 8px;
}

.review-time {
  font-size: 12px;
  color: var(--text-secondary);
}
</style>




