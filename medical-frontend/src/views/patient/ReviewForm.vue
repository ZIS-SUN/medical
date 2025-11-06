<template>
  <div class="review-form-page">
    <div class="page-container">
      <h1 class="page-title">评价医生</h1>

      <el-card class="review-card">
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item label="预约信息">
            <div class="appointment-info" v-if="appointment">
              <p>医生：{{ appointment.doctorName }}</p>
              <p>时间：{{ appointment.appointmentDate }} {{ appointment.timeSlot }}</p>
            </div>
          </el-form-item>

          <el-form-item label="评分" prop="rating">
            <el-rate v-model="form.rating" :size="32" />
          </el-form-item>

          <el-form-item label="评价内容" prop="content">
            <el-input
              v-model="form.content"
              type="textarea"
              :rows="6"
              placeholder="请输入您的评价，分享就诊体验，帮助其他患者选择合适的医生"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleSubmit">
              提交评价
            </el-button>
            <el-button @click="handleCancel">
              取消
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAppointmentDetail } from '@/api/appointment'
import { createReview } from '@/api/review'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const loading = ref(false)
const appointment = ref(null)

const form = reactive({
  appointmentId: '',
  doctorId: '',
  rating: 5,
  content: ''
})

const rules = {
  rating: [
    { required: true, message: '请选择评分', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' },
    { min: 10, message: '评价内容至少10个字符', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  await formRef.value.validate()
  
  loading.value = true
  try {
    await createReview(form)
    ElMessage.success('评价成功')
    router.push('/my-appointments')
  } catch (error) {
    ElMessage.error(error.message || '评价失败')
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  router.back()
}

onMounted(async () => {
  const appointmentId = route.params.appointmentId
  try {
    appointment.value = await getAppointmentDetail(appointmentId)
    form.appointmentId = appointmentId
    form.doctorId = appointment.value.doctorId
  } catch (error) {
    ElMessage.error('加载预约信息失败')
  }
})
</script>

<style scoped>
.review-card {
  max-width: 600px;
  margin: 0 auto;
}

.appointment-info {
  padding: 12px;
  background-color: var(--bg-color);
  border-radius: var(--border-radius);
}

.appointment-info p {
  margin: 8px 0;
  color: var(--text-regular);
}
</style>




