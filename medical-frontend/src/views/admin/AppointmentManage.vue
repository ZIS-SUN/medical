<template>
  <div class="appointment-manage-page">
    <h1 class="page-title">预约管理</h1>

    <el-card class="filter-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待确认" value="待确认" />
            <el-option label="已确认" value="已确认" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已取消" value="已取消" />
          </el-select>
        </el-form-item>
        <el-form-item label="患者姓名">
          <el-input v-model="searchForm.userName" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="appointments" v-loading="loading" style="width: 100%">
        <el-table-column prop="appointmentNo" label="预约单号" width="180" />
        <el-table-column prop="userName" label="患者" width="100" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="departmentName" label="科室" width="100" />
        <el-table-column prop="appointmentDate" label="预约日期" width="120" />
        <el-table-column prop="timeSlot" label="时间段" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === '待确认'"
              size="small"
              type="success"
              @click="handleConfirm(row)"
            >
              确认
            </el-button>
            <el-button
              v-if="row.status === '已确认'"
              size="small"
              type="primary"
              @click="handleComplete(row)"
            >
              完成
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
          @size-change="fetchAppointments"
          @current-change="fetchAppointments"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import {
  getAppointmentList,
  confirmAppointment,
  completeAppointment
} from '@/api/appointment'
import { ElMessage } from 'element-plus'

const appointments = ref([])
const loading = ref(false)

const searchForm = reactive({
  status: '',
  userName: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const getStatusType = (status) => {
  const map = {
    '待确认': 'warning',
    '已确认': 'primary',
    '就诊中': 'info',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return map[status] || 'info'
}

const fetchAppointments = async () => {
  loading.value = true
  try {
    const { list, total } = await getAppointmentList({
      ...searchForm,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    appointments.value = list
    pagination.total = total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchAppointments()
}

const handleReset = () => {
  searchForm.status = ''
  searchForm.userName = ''
  pagination.page = 1
  fetchAppointments()
}

const handleConfirm = async (row) => {
  try {
    await confirmAppointment(row.id)
    ElMessage.success('确认成功')
    fetchAppointments()
  } catch (error) {
    ElMessage.error(error.message || '确认失败')
  }
}

const handleComplete = async (row) => {
  try {
    await completeAppointment(row.id)
    ElMessage.success('完成成功')
    fetchAppointments()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

onMounted(() => {
  fetchAppointments()
})
</script>

<style scoped>
.filter-card {
  margin-bottom: 20px;
}
</style>




