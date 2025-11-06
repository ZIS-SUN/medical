<template>
  <div class="record-manage-page">
    <h1 class="page-title">病历管理</h1>

    <el-card class="filter-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="病历号">
          <el-input v-model="searchForm.recordNo" placeholder="请输入病历号" clearable />
        </el-form-item>
        <el-form-item label="患者姓名">
          <el-input v-model="searchForm.userName" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="医生姓名">
          <el-input v-model="searchForm.doctorName" placeholder="请输入医生姓名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="records" v-loading="loading" style="width: 100%">
        <el-table-column prop="recordNo" label="病历号" width="160" />
        <el-table-column prop="userName" label="患者" width="100" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="departmentName" label="科室" width="120" />
        <el-table-column prop="chiefComplaint" label="主诉" min-width="150" show-overflow-tooltip />
        <el-table-column prop="diagnosis" label="诊断" min-width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">
              删除
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
          @size-change="fetchRecords"
          @current-change="fetchRecords"
        />
      </div>
    </el-card>

    <!-- 病历详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="病历详情" width="800px">
      <div v-if="currentRecord" class="record-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="病历号" :span="2">
            <el-tag>{{ currentRecord.recordNo }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="患者姓名">{{ currentRecord.userName }}</el-descriptions-item>
          <el-descriptions-item label="医生姓名">{{ currentRecord.doctorName }}</el-descriptions-item>
          <el-descriptions-item label="科室" :span="2">{{ currentRecord.departmentName }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ currentRecord.createTime }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">病情信息</el-divider>
        
        <div class="detail-section">
          <h4>主诉</h4>
          <p>{{ currentRecord.chiefComplaint }}</p>
        </div>

        <div class="detail-section">
          <h4>现病史</h4>
          <p>{{ currentRecord.presentIllness || '-' }}</p>
        </div>

        <div class="detail-section">
          <h4>诊断结果</h4>
          <p class="diagnosis">{{ currentRecord.diagnosis }}</p>
        </div>

        <el-divider content-position="left">治疗方案</el-divider>

        <div class="detail-section">
          <h4>处方</h4>
          <p>{{ currentRecord.prescription || '-' }}</p>
        </div>

        <div class="detail-section">
          <h4>医嘱</h4>
          <p>{{ currentRecord.advice || '-' }}</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getRecordList, deleteRecord } from '@/api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const records = ref([])
const loading = ref(false)
const detailDialogVisible = ref(false)
const currentRecord = ref(null)

const searchForm = reactive({
  recordNo: '',
  userName: '',
  doctorName: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 获取病历列表
const fetchRecords = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getRecordList(params)
    records.value = res.list || []
    pagination.total = res.total || 0
  } catch (error) {
    console.error('获取病历列表失败:', error)
    ElMessage.error('获取病历列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchRecords()
}

// 重置
const handleReset = () => {
  searchForm.recordNo = ''
  searchForm.userName = ''
  searchForm.doctorName = ''
  pagination.page = 1
  fetchRecords()
}

// 查看详情
const handleViewDetail = (row) => {
  currentRecord.value = row
  detailDialogVisible.value = true
}

// 删除病历
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除病历【${row.recordNo}】吗？删除后将无法恢复。`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteRecord(row.id)
      ElMessage.success('删除成功')
      fetchRecords()
    } catch (error) {
      console.error('删除病历失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消操作
  })
}

onMounted(() => {
  fetchRecords()
})
</script>

<style scoped>
.record-manage-page {
  padding: 20px;
}

.page-title {
  margin: 0 0 20px 0;
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.filter-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.record-detail {
  max-height: 600px;
  overflow-y: auto;
}

.detail-section {
  margin: 15px 0;
}

.detail-section h4 {
  margin: 10px 0;
  font-size: 14px;
  font-weight: 600;
  color: #666;
}

.detail-section p {
  margin: 5px 0;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}

.diagnosis {
  color: #e6a23c;
  font-weight: 500;
}
</style>
