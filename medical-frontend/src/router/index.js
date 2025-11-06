import { createRouter, createWebHistory } from 'vue-router'
import { getToken, getAdminToken } from '@/utils/auth'
import { ElMessage } from 'element-plus'

// 患者端布局
import PatientLayout from '@/components/Layout/PatientLayout.vue'

// 患者端页面
import Home from '@/views/patient/Home.vue'
import Login from '@/views/patient/Login.vue'
import Register from '@/views/patient/Register.vue'
import DepartmentList from '@/views/patient/DepartmentList.vue'
import DoctorList from '@/views/patient/DoctorList.vue'
import DoctorDetail from '@/views/patient/DoctorDetail.vue'
import Consultation from '@/views/patient/Consultation.vue'
import ConsultationList from '@/views/patient/ConsultationList.vue'
import AppointmentList from '@/views/patient/AppointmentList.vue'
import MedicalRecordList from '@/views/patient/MedicalRecordList.vue'
import Profile from '@/views/patient/Profile.vue'
import ReviewForm from '@/views/patient/ReviewForm.vue'

// 管理端布局
import AdminLayout from '@/components/Layout/AdminLayout.vue'

// 管理端页面
import AdminLogin from '@/views/admin/Login.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import DoctorManage from '@/views/admin/DoctorManage.vue'
import ScheduleManage from '@/views/admin/ScheduleManage.vue'
import AppointmentManage from '@/views/admin/AppointmentManage.vue'
import ConsultationManage from '@/views/admin/ConsultationManage.vue'
import RecordManage from '@/views/admin/RecordManage.vue'
import AnnouncementManage from '@/views/admin/AnnouncementManage.vue'
import UserManage from '@/views/admin/UserManage.vue'

const routes = [
  // 患者端路由
  {
    path: '/',
    component: PatientLayout,
    children: [
      {
        path: '',
        name: 'Home',
        component: Home
      },
      {
        path: 'departments',
        name: 'DepartmentList',
        component: DepartmentList
      },
      {
        path: 'doctors',
        name: 'DoctorList',
        component: DoctorList
      },
      {
        path: 'doctors/:id',
        name: 'DoctorDetail',
        component: DoctorDetail
      },
      {
        path: 'consultation',
        name: 'Consultation',
        component: Consultation,
        meta: { requiresAuth: true }
      },
      {
        path: 'my-consultations',
        name: 'ConsultationList',
        component: ConsultationList,
        meta: { requiresAuth: true }
      },
      {
        path: 'my-appointments',
        name: 'AppointmentList',
        component: AppointmentList,
        meta: { requiresAuth: true }
      },
      {
        path: 'my-records',
        name: 'MedicalRecordList',
        component: MedicalRecordList,
        meta: { requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile,
        meta: { requiresAuth: true }
      },
      {
        path: 'review/:appointmentId',
        name: 'ReviewForm',
        component: ReviewForm,
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },

  // 管理端路由
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: 'doctors',
        name: 'DoctorManage',
        component: DoctorManage
      },
      {
        path: 'schedules',
        name: 'ScheduleManage',
        component: ScheduleManage
      },
      {
        path: 'appointments',
        name: 'AppointmentManage',
        component: AppointmentManage
      },
      {
        path: 'consultations',
        name: 'ConsultationManage',
        component: ConsultationManage
      },
      {
        path: 'records',
        name: 'RecordManage',
        component: RecordManage
      },
      {
        path: 'announcements',
        name: 'AnnouncementManage',
        component: AnnouncementManage
      },
      {
        path: 'users',
        name: 'UserManage',
        component: UserManage
      }
    ]
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AdminLogin
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 需要认证的页面
  if (to.meta.requiresAuth) {
    // 管理端需要管理员token
    if (to.meta.requiresAdmin) {
      const adminToken = getAdminToken()
      if (!adminToken) {
        ElMessage.warning('请先登录管理员账号')
        next('/admin/login')
        return
      }
    } else {
      // 患者端需要用户token
      const token = getToken()
      if (!token) {
        ElMessage.warning('请先登录')
        next('/login')
        return
      }
    }
  }

  next()
})

export default router




