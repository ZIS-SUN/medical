<template>
  <div class="consultation-page">
    <div class="page-container">
      <h1 class="page-title">在线问诊</h1>

      <el-card class="consultation-card">
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item label="选择医生" prop="doctorId">
            <el-select 
              v-model="form.doctorId" 
              placeholder="请选择医生"
              filterable
              style="width: 100%"
            >
              <el-option
                v-for="doctor in doctors"
                :key="doctor.id"
                :label="`${doctor.name} - ${doctor.title} - ${doctor.departmentName}`"
                :value="doctor.id"
              >
                <div class="doctor-option">
                  <span>{{ doctor.name }}</span>
                  <span class="doctor-title">{{ doctor.title }}</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="问题描述" prop="question">
            <el-input
              v-model="form.question"
              type="textarea"
              :rows="8"
              placeholder="请详细描述您的症状、持续时间、是否有其他症状等信息，以便医生更准确地诊断"
            />
          </el-form-item>

          <el-form-item label="上传图片">
            <el-upload
              v-model:file-list="fileList"
              :action="uploadUrl"
              :headers="headers"
              list-type="picture-card"
              :limit="3"
              :on-success="handleUploadSuccess"
              :on-remove="handleRemove"
              :before-upload="beforeUpload"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
            <div class="upload-tip">最多上传3张图片，单张不超过5MB</div>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="loading"
              @click="handleSubmit"
            >
              提交问诊
            </el-button>
            <el-button size="large" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getDoctorList } from '@/api/doctor'
import { createConsultation } from '@/api/consultation'
import { getUploadUrl } from '@/api/upload'
import { getToken } from '@/utils/auth'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const router = useRouter()
const formRef = ref()
const doctors = ref([])
const fileList = ref([])
const loading = ref(false)

const form = reactive({
  doctorId: '',
  question: '',
  images: []
})

const rules = {
  doctorId: [
    { required: true, message: '请选择医生', trigger: 'change' }
  ],
  question: [
    { required: true, message: '请输入问题描述', trigger: 'blur' },
    { min: 10, message: '问题描述至少10个字符', trigger: 'blur' }
  ]
}

const uploadUrl = getUploadUrl()

const headers = computed(() => {
  const token = getToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
})

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

const handleUploadSuccess = (response) => {
  if (response.code === 0) {
    form.images.push(response.data)
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

const handleRemove = (file) => {
  const url = file.response?.data || file.url
  const index = form.images.indexOf(url)
  if (index > -1) {
    form.images.splice(index, 1)
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  
  loading.value = true
  try {
    await createConsultation({
      ...form,
      images: form.images.join(',')
    })

    ElMessage.success('提交成功，请等待医生回复')
    router.push('/my-consultations')
  } catch (error) {
    ElMessage.error(error.message || '提交失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  formRef.value.resetFields()
  fileList.value = []
  form.images = []
}

onMounted(async () => {
  try {
    const { list } = await getDoctorList({ page: 1, pageSize: 100 })
    doctors.value = list
  } catch (error) {
    ElMessage.error('加载医生列表失败')
  }
})
</script>

<style scoped>
.consultation-card {
  max-width: 800px;
  margin: 0 auto;
}

.doctor-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.doctor-title {
  font-size: 12px;
  color: var(--text-secondary);
}

.upload-tip {
  color: var(--text-secondary);
  font-size: 12px;
  margin-top: 8px;
}
</style>




