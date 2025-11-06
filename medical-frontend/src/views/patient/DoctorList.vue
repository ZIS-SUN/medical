<template>
  <div class="doctor-list-page">
    <div class="page-container">
      <h1 class="page-title">医生列表</h1>

      <!-- 筛选条件 -->
      <el-card class="filter-card">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="科室">
            <el-select v-model="searchForm.departmentId" placeholder="请选择科室" clearable>
              <el-option
                v-for="dept in departments"
                :key="dept.id"
                :label="dept.name"
                :value="dept.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="职称">
            <el-select v-model="searchForm.title" placeholder="请选择职称" clearable>
              <el-option label="主任医师" value="主任医师" />
              <el-option label="副主任医师" value="副主任医师" />
              <el-option label="主治医师" value="主治医师" />
              <el-option label="医师" value="医师" />
            </el-select>
          </el-form-item>

          <el-form-item label="搜索">
            <el-input v-model="searchForm.keyword" placeholder="输入医生姓名" clearable />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 医生列表 -->
      <div v-loading="loading" class="doctor-list">
        <el-row :gutter="20">
          <el-col :span="6" v-for="doctor in doctors" :key="doctor.id">
            <DoctorCard :doctor="doctor" />
          </el-col>
        </el-row>

        <el-empty v-if="!loading && doctors.length === 0" description="暂无医生数据" />
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[12, 24, 36, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchDoctors"
          @current-change="fetchDoctors"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getDoctorList } from '@/api/doctor'
import { getDepartments } from '@/api/department'
import DoctorCard from '@/components/DoctorCard.vue'

const route = useRoute()
const doctors = ref([])
const departments = ref([])
const loading = ref(false)

const searchForm = reactive({
  departmentId: '',
  title: '',
  keyword: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 12,
  total: 0
})

const fetchDoctors = async () => {
  loading.value = true
  try {
    const { list, total } = await getDoctorList({
      ...searchForm,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    doctors.value = list
    pagination.total = total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchDoctors()
}

const handleReset = () => {
  searchForm.departmentId = ''
  searchForm.title = ''
  searchForm.keyword = ''
  pagination.page = 1
  fetchDoctors()
}

onMounted(async () => {
  // 从路由获取科室ID
  if (route.query.departmentId) {
    searchForm.departmentId = Number(route.query.departmentId)
  }
  
  const deptData = await getDepartments()
  departments.value = deptData
  
  fetchDoctors()
})
</script>

<style scoped>
.filter-card {
  margin-bottom: 20px;
}

.doctor-list {
  min-height: 400px;
  margin-bottom: 20px;
}
</style>




