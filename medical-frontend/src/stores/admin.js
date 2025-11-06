import { defineStore } from 'pinia'
import { adminLogin } from '@/api/auth'
import { getAdminToken, setAdminToken, removeAdminToken } from '@/utils/auth'

export const useAdminStore = defineStore('admin', {
  state: () => ({
    token: getAdminToken(),
    adminInfo: null
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    adminName: (state) => state.adminInfo?.username || ''
  },

  actions: {
    async login(loginForm) {
      const { token, adminInfo } = await adminLogin(loginForm)
      this.token = token
      this.adminInfo = adminInfo
      setAdminToken(token)
    },

    logout() {
      this.token = ''
      this.adminInfo = null
      removeAdminToken()
    }
  }
})




