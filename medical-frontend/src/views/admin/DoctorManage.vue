<template>
  <div class="doctor-manage-page">
    <h1 class="page-title">医生管理</h1>

    <el-card class="filter-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="医生姓名">
          <el-input v-model="searchForm.name" placeholder="请输入医生姓名" clearable />
        </el-form-item>
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
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增医生</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="doctors" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="title" label="职称" width="120" />
        <el-table-column prop="departmentName" label="科室" width="120" />
        <el-table-column prop="phone" label="联系电话" width="120" />
        <el-table-column prop="avgRating" label="评分" width="100">
          <template #default="{ row }">
            <el-rate v-model="row.avgRating" disabled :allow-half="true" :size="14" />
          </template>
        </el-table-column>
        <el-table-column prop="profile" label="简介" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchDoctors"
          @current-change="fetchDoctors"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-select v-model="form.title" style="width: 100%">
            <el-option label="主任医师" value="主任医师" />
            <el-option label="副主任医师" value="副主任医师" />
            <el-option label="主治医师" value="主治医师" />
            <el-option label="医师" value="医师" />
          </el-select>
        </el-form-item>
        <el-form-item label="科室" prop="departmentId">
          <el-select v-model="form.departmentId" style="width: 100%">
            <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="简介" prop="profile">
          <el-input v-model="form.profile" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getDoctorList, createDoctor, updateDoctor, deleteDoctor } from '@/api/doctor'
import { getDepartments } from '@/api/department'
import { ElMessage, ElMessageBox } from 'element-plus'
import { validatePhone } from '@/utils/validate'

const doctors = ref([])
const departments = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增医生')
const formRef = ref()
const submitLoading = ref(false)
const currentId = ref(null)

const searchForm = reactive({
  name: '',
  departmentId: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const form = reactive({
  name: '',
  gender: '男',
  title: '',
  departmentId: '',
  phone: '',
  email: '',
  profile: ''
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  title: [{ required: true, message: '请选择职称', trigger: 'change' }],
  departmentId: [{ required: true, message: '请选择科室', trigger: 'change' }],
  phone: [{ required: true, validator: validatePhone, trigger: 'blur' }]
}

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
  searchForm.name = ''
  searchForm.departmentId = ''
  pagination.page = 1
  fetchDoctors()
}

const handleAdd = () => {
  dialogTitle.value = '新增医生'
  currentId.value = null
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑医生'
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该医生吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteDoctor(row.id)
    ElMessage.success('删除成功')
    fetchDoctors()
  }).catch(() => {})
}

const handleSubmit = async () => {
  await formRef.value.validate()
  
  submitLoading.value = true
  try {
    if (currentId.value) {
      await updateDoctor(currentId.value, form)
      ElMessage.success('更新成功')
    } else {
      await createDoctor(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchDoctors()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

const resetForm = () => {
  form.name = ''
  form.gender = '男'
  form.title = ''
  form.departmentId = ''
  form.phone = ''
  form.email = ''
  form.profile = ''
}

onMounted(async () => {
  departments.value = await getDepartments()
  fetchDoctors()
})
</script>

<style scoped>
.filter-card {
  margin-bottom: 20px;
}
</style>




