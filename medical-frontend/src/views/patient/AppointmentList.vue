<template>
  <div class="appointment-list-page">
    <div class="page-container">
      <h1 class="page-title">我的预约</h1>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="待确认" name="待确认" />
        <el-tab-pane label="已确认" name="已确认" />
        <el-tab-pane label="已完成" name="已完成" />
        <el-tab-pane label="已取消" name="已取消" />
      </el-tabs>

      <div v-loading="loading" class="appointment-list">
        <AppointmentCard
          v-for="item in appointments"
          :key="item.id"
          :appointment="item"
          @cancel="handleCancel"
          @viewRecord="handleViewRecord"
          @review="handleReview"
        />

        <el-empty v-if="!loading && appointments.length === 0" description="暂无预约记录" />
      </div>

      <div class="pagination-container" v-if="pagination.total > 0">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, prev, pager, next"
          @current-change="fetchAppointments"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyAppointments, cancelAppointment } from '@/api/appointment'
import { ElMessageBox, ElMessage } from 'element-plus'
import AppointmentCard from '@/components/AppointmentCard.vue'

const router = useRouter()
const activeTab = ref('all')
const appointments = ref([])
const loading = ref(false)

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const fetchAppointments = async () => {
  loading.value = true
  try {
    const status = activeTab.value === 'all' ? '' : activeTab.value
    const { list, total } = await getMyAppointments({
      status,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    appointments.value = list
    pagination.total = total
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  pagination.page = 1
  fetchAppointments()
}

const handleCancel = (item) => {
  ElMessageBox.prompt('请输入取消原因', '取消预约', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputType: 'textarea',
    inputValidator: (value) => {
      if (!value) {
        return '请输入取消原因'
      }
      return true
    }
  }).then(async ({ value }) => {
    await cancelAppointment(item.id, { cancelReason: value })
    ElMessage.success('取消成功')
    fetchAppointments()
  }).catch(() => {
    // 用户取消操作
  })
}

const handleViewRecord = (item) => {
  router.push(`/my-records?appointmentId=${item.id}`)
}

const handleReview = (item) => {
  router.push(`/review/${item.id}`)
}

onMounted(() => {
  fetchAppointments()
})
</script>

<style scoped>
.appointment-list {
  min-height: 400px;
  margin-top: 20px;
}
</style>




