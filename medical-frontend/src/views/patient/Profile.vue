<template>
  <div class="profile-page">
    <div class="page-container">
      <h1 class="page-title">个人中心</h1>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-card>
            <div class="profile-sidebar">
              <el-avatar :size="100" :src="userInfo.avatar">
                {{ userInfo.username?.charAt(0) }}
              </el-avatar>
              <h3 class="username">{{ userInfo.username }}</h3>
              <p class="user-phone">{{ userInfo.phone }}</p>
            </div>
          </el-card>
        </el-col>

        <el-col :span="18">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="基本信息" name="info">
                <el-form
                  ref="formRef"
                  :model="form"
                  :rules="rules"
                  label-width="100px"
                  class="profile-form"
                >
                  <el-form-item label="用户名">
                    <el-input v-model="form.username" disabled />
                  </el-form-item>

                  <el-form-item label="真实姓名" prop="realName">
                    <el-input v-model="form.realName" />
                  </el-form-item>

                  <el-form-item label="手机号" prop="phone">
                    <el-input v-model="form.phone" />
                  </el-form-item>

                  <el-form-item label="身份证号" prop="idCard">
                    <el-input v-model="form.idCard" />
                  </el-form-item>

                  <el-form-item label="性别" prop="gender">
                    <el-radio-group v-model="form.gender">
                      <el-radio label="男">男</el-radio>
                      <el-radio label="女">女</el-radio>
                    </el-radio-group>
                  </el-form-item>

                  <el-form-item label="出生日期" prop="birthDate">
                    <el-date-picker
                      v-model="form.birthDate"
                      type="date"
                      placeholder="选择日期"
                      style="width: 100%"
                    />
                  </el-form-item>

                  <el-form-item label="地址">
                    <el-input v-model="form.address" />
                  </el-form-item>

                  <el-form-item>
                    <el-button type="primary" :loading="loading" @click="handleUpdate">
                      保存修改
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>

              <el-tab-pane label="修改密码" name="password">
                <el-form
                  ref="passwordFormRef"
                  :model="passwordForm"
                  :rules="passwordRules"
                  label-width="100px"
                  class="profile-form"
                >
                  <el-form-item label="原密码" prop="oldPassword">
                    <el-input
                      v-model="passwordForm.oldPassword"
                      type="password"
                      show-password
                    />
                  </el-form-item>

                  <el-form-item label="新密码" prop="newPassword">
                    <el-input
                      v-model="passwordForm.newPassword"
                      type="password"
                      show-password
                    />
                  </el-form-item>

                  <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input
                      v-model="passwordForm.confirmPassword"
                      type="password"
                      show-password
                    />
                  </el-form-item>

                  <el-form-item>
                    <el-button type="primary" :loading="passwordLoading" @click="handleUpdatePassword">
                      修改密码
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { updateProfile, updatePassword } from '@/api/auth'
import { validatePhone, validateIdCard } from '@/utils/validate'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const formRef = ref()
const passwordFormRef = ref()
const activeTab = ref('info')
const loading = ref(false)
const passwordLoading = ref(false)

const userInfo = computed(() => userStore.userInfo || {})

const form = reactive({
  username: '',
  realName: '',
  phone: '',
  idCard: '',
  gender: '男',
  birthDate: '',
  address: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, validator: validatePhone, trigger: 'blur' }
  ],
  idCard: [
    { required: true, validator: validateIdCard, trigger: 'blur' }
  ]
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleUpdate = async () => {
  await formRef.value.validate()
  
  loading.value = true
  try {
    await updateProfile(form)
    await userStore.fetchUserInfo()
    ElMessage.success('更新成功')
  } catch (error) {
    ElMessage.error(error.message || '更新失败')
  } finally {
    loading.value = false
  }
}

const handleUpdatePassword = async () => {
  await passwordFormRef.value.validate()
  
  passwordLoading.value = true
  try {
    await updatePassword(passwordForm)
    ElMessage.success('密码修改成功，请重新登录')
    // 清空表单
    passwordFormRef.value.resetFields()
  } catch (error) {
    ElMessage.error(error.message || '修改失败')
  } finally {
    passwordLoading.value = false
  }
}

onMounted(() => {
  // 加载用户信息到表单
  Object.assign(form, userInfo.value)
})
</script>

<style scoped>
.profile-sidebar {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px 0;
}

.username {
  font-size: 18px;
  color: var(--text-primary);
  margin: 0;
}

.user-phone {
  font-size: 14px;
  color: var(--text-secondary);
}

.profile-form {
  max-width: 600px;
  padding: 20px 0;
}
</style>




