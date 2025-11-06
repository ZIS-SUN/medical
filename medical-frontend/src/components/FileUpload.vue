<template>
  <div class="file-upload">
    <el-upload
      v-model:file-list="fileList"
      :action="uploadUrl"
      :headers="headers"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :on-remove="handleRemove"
      :limit="limit"
      :accept="accept"
      :list-type="listType"
    >
      <template v-if="listType === 'picture-card'">
        <el-icon><Plus /></el-icon>
      </template>
      <template v-else>
        <el-button type="primary">
          <el-icon><Upload /></el-icon>
          点击上传
        </el-button>
      </template>
      <template #tip>
        <div class="el-upload__tip" v-if="tip">
          {{ tip }}
        </div>
      </template>
    </el-upload>
  </div>
</template>

<script setup>
import { ref, computed, defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Upload } from '@element-plus/icons-vue'
import { getToken } from '@/utils/auth'
import { getUploadUrl } from '@/api/upload'

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
  limit: {
    type: Number,
    default: 1
  },
  accept: {
    type: String,
    default: 'image/*'
  },
  maxSize: {
    type: Number,
    default: 5 // MB
  },
  listType: {
    type: String,
    default: 'picture-card'
  },
  tip: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

const fileList = ref([])
const uploadUrl = getUploadUrl()

const headers = computed(() => {
  const token = getToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
})

const beforeUpload = (file) => {
  const isLt = file.size / 1024 / 1024 < props.maxSize
  if (!isLt) {
    ElMessage.error(`上传文件大小不能超过 ${props.maxSize}MB!`)
    return false
  }
  return true
}

const handleSuccess = (response, file) => {
  if (response.code === 0) {
    const newFiles = [...props.modelValue, response.data]
    emit('update:modelValue', newFiles)
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

const handleError = () => {
  ElMessage.error('上传失败')
}

const handleRemove = (file) => {
  const newFiles = props.modelValue.filter(url => url !== file.url && url !== file.response?.data)
  emit('update:modelValue', newFiles)
}
</script>

<style scoped>
.file-upload {
  width: 100%;
}
</style>




