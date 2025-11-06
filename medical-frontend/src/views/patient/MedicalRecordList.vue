<template>
  <div class="medical-record-list-page">
    <div class="page-container">
      <h1 class="page-title">电子病历</h1>

      <div v-loading="loading" class="record-list">
        <el-card
          v-for="item in records"
          :key="item.id"
          class="record-card"
          shadow="hover"
        >
          <div class="card-header">
            <h3>{{ item.diagnosis }}</h3>
            <span class="record-date">{{ item.visitDate }}</span>
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
              <span class="label">主诉：</span>
              <span class="value">{{ item.complaint }}</span>
            </div>
            <div class="info-row">
              <span class="label">诊断：</span>
              <span class="value">{{ item.diagnosis }}</span>
            </div>
            <div class="info-row">
              <span class="label">处方：</span>
              <span class="value">{{ item.prescription }}</span>
            </div>
            <div class="info-row" v-if="item.advice">
              <span class="label">医嘱：</span>
              <span class="value">{{ item.advice }}</span>
            </div>
          </div>

          <div class="card-footer">
            <el-button size="small" type="primary" @click="handlePrint(item)">
              打印病历
            </el-button>
          </div>
        </el-card>

        <el-empty v-if="!loading && records.length === 0" description="暂无病历记录" />
      </div>

      <div class="pagination-container" v-if="pagination.total > 0">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, prev, pager, next"
          @current-change="fetchRecords"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getMyRecords } from '@/api/record'
import { ElMessage } from 'element-plus'

const route = useRoute()
const records = ref([])
const loading = ref(false)

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const fetchRecords = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize
    }
    
    // 如果有预约ID参数，只查询该预约的病历
    if (route.query.appointmentId) {
      params.appointmentId = route.query.appointmentId
    }
    
    const { list, total } = await getMyRecords(params)
    records.value = list
    pagination.total = total
  } finally {
    loading.value = false
  }
}

const handlePrint = (item) => {
  ElMessage.info('打印功能开发中')
  // TODO: 实现打印功能
}

onMounted(() => {
  fetchRecords()
})
</script>

<style scoped>
.record-list {
  min-height: 400px;
}

.record-card {
  margin-bottom: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  color: var(--text-primary);
  font-size: 18px;
}

.record-date {
  color: var(--text-secondary);
  font-size: 14px;
}

.card-body {
  padding: 12px 0;
}

.info-row {
  display: flex;
  margin-bottom: 12px;
  font-size: 14px;
  line-height: 1.6;
}

.info-row:last-child {
  margin-bottom: 0;
}

.label {
  color: var(--text-secondary);
  min-width: 80px;
  flex-shrink: 0;
}

.value {
  color: var(--text-primary);
  flex: 1;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
}
</style>




