<template>
  <div class="consultation-manage-page">
    <h1 class="page-title">问诊管理</h1>

    <el-card class="filter-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待回复" value="待回复" />
            <el-option label="已回复" value="已回复" />
            <el-option label="已关闭" value="已关闭" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="consultations" v-loading="loading" style="width: 100%">
        <el-table-column prop="consultationNo" label="问诊单号" width="180" />
        <el-table-column prop="userName" label="患者" width="100" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="question" label="问题描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="160" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button
              v-if="row.status === '待回复'"
              size="small"
              type="primary"
              @click="handleReply(row)"
            >
              回复
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchConsultations"
          @current-change="fetchConsultations"
        />
      </div>
    </el-card>

    <!-- 回复对话框 -->
    <el-dialog v-model="dialogVisible" title="回复问诊" width="600px">
      <el-form :model="replyForm" label-width="100px">
        <el-form-item label="问题描述">
          <div class="question-text">{{ currentConsultation?.question }}</div>
        </el-form-item>
        <el-form-item label="回复内容">
          <el-input
            v-model="replyForm.reply"
            type="textarea"
            :rows="6"
            placeholder="请输入回复内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmitReply">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getConsultationList, replyConsultation } from '@/api/consultation'
import { ElMessage } from 'element-plus'

const consultations = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const currentConsultation = ref(null)

const searchForm = reactive({
  status: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const replyForm = reactive({
  reply: ''
})

const getStatusType = (status) => {
  const map = {
    '待回复': 'warning',
    '已回复': 'success',
    '已关闭': 'info'
  }
  return map[status] || 'info'
}

const fetchConsultations = async () => {
  loading.value = true
  try {
    const { list, total } = await getConsultationList({
      ...searchForm,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    consultations.value = list
    pagination.total = total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchConsultations()
}

const handleReset = () => {
  searchForm.status = ''
  pagination.page = 1
  fetchConsultations()
}

const handleView = (row) => {
  ElMessage.info('查看详情功能开发中')
}

const handleReply = (row) => {
  currentConsultation.value = row
  replyForm.reply = ''
  dialogVisible.value = true
}

const handleSubmitReply = async () => {
  if (!replyForm.reply) {
    return ElMessage.warning('请输入回复内容')
  }

  submitLoading.value = true
  try {
    await replyConsultation(currentConsultation.value.id, replyForm)
    ElMessage.success('回复成功')
    dialogVisible.value = false
    fetchConsultations()
  } catch (error) {
    ElMessage.error(error.message || '回复失败')
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  fetchConsultations()
})
</script>

<style scoped>
.filter-card {
  margin-bottom: 20px;
}

.question-text {
  padding: 12px;
  background-color: var(--bg-color);
  border-radius: var(--border-radius);
  line-height: 1.6;
}
</style>




