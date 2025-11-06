<template>
  <div class="consultation-list-page">
    <div class="page-container">
      <h1 class="page-title">我的问诊</h1>

      <div v-loading="loading" class="consultation-list">
        <el-card
          v-for="item in consultations"
          :key="item.id"
          class="consultation-card"
          shadow="hover"
        >
          <div class="card-header">
            <span class="consultation-no">问诊单号：{{ item.consultationNo }}</span>
            <el-tag :type="getStatusType(item.status)">{{ item.status }}</el-tag>
          </div>

          <el-divider />

          <div class="card-body">
            <div class="info-row">
              <span class="label">医生：</span>
              <span class="value">{{ item.doctorName }} - {{ item.title }}</span>
            </div>
            <div class="info-row">
              <span class="label">科室：</span>
              <span class="value">{{ item.departmentName }}</span>
            </div>
            <div class="info-row">
              <span class="label">问题描述：</span>
              <span class="value">{{ item.question }}</span>
            </div>
            <div class="info-row" v-if="item.images">
              <span class="label">图片：</span>
              <div class="images">
                <ImagePreview :images="item.images.split(',')" />
              </div>
            </div>
            <div class="info-row" v-if="item.reply">
              <span class="label">医生回复：</span>
              <span class="value reply">{{ item.reply }}</span>
            </div>
            <div class="info-row">
              <span class="label">提交时间：</span>
              <span class="value">{{ item.createTime }}</span>
            </div>
            <div class="info-row" v-if="item.replyTime">
              <span class="label">回复时间：</span>
              <span class="value">{{ item.replyTime }}</span>
            </div>
          </div>

          <div class="card-footer">
            <el-button size="small" @click="handleViewDetail(item)">
              查看详情
            </el-button>
          </div>
        </el-card>

        <el-empty v-if="!loading && consultations.length === 0" description="暂无问诊记录" />
      </div>

      <div class="pagination-container" v-if="pagination.total > 0">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, prev, pager, next"
          @current-change="fetchConsultations"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyConsultations } from '@/api/consultation'
import ImagePreview from '@/components/ImagePreview.vue'

const router = useRouter()
const consultations = ref([])
const loading = ref(false)

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const fetchConsultations = async () => {
  loading.value = true
  try {
    const { list, total } = await getMyConsultations({
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    consultations.value = list
    pagination.total = total
  } finally {
    loading.value = false
  }
}

const getStatusType = (status) => {
  const map = {
    '待回复': 'warning',
    '已回复': 'success',
    '已关闭': 'info'
  }
  return map[status] || 'info'
}

const handleViewDetail = (item) => {
  router.push(`/consultations/${item.id}`)
}

onMounted(() => {
  fetchConsultations()
})
</script>

<style scoped>
.consultation-list {
  min-height: 400px;
}

.consultation-card {
  margin-bottom: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.consultation-no {
  font-size: 14px;
  color: var(--text-primary);
  font-weight: 500;
}

.card-body {
  padding: 12px 0;
}

.info-row {
  display: flex;
  margin-bottom: 12px;
  font-size: 14px;
}

.info-row:last-child {
  margin-bottom: 0;
}

.label {
  color: var(--text-secondary);
  min-width: 100px;
}

.value {
  color: var(--text-primary);
  flex: 1;
  word-break: break-all;
}

.value.reply {
  background-color: #f0f9ff;
  padding: 12px;
  border-radius: 4px;
  color: var(--primary-color);
}

.images {
  flex: 1;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
}
</style>




