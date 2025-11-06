import { defineStore } from 'pinia'
import { login, register, getUserInfo as fetchUserInfo } from '@/api/auth'
import { getToken, setToken, removeToken, getUserInfo as getLocalUserInfo, setUserInfo } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userInfo: getLocalUserInfo()
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    username: (state) => state.userInfo?.username || '',
    userId: (state) => state.userInfo?.id || null,
    avatar: (state) => state.userInfo?.avatar || '',
    phone: (state) => state.userInfo?.phone || ''
  },

  actions: {
    async login(loginForm) {
      const { token, userInfo } = await login(loginForm)
      this.token = token
      this.userInfo = userInfo
      setToken(token)
      setUserInfo(userInfo)
    },

    async register(registerForm) {
      await register(registerForm)
    },

    async fetchUserInfo() {
      const userInfo = await fetchUserInfo()
      this.userInfo = userInfo
      setUserInfo(userInfo)
    },

    logout() {
      this.token = ''
      this.userInfo = null
      removeToken()
    }
  }
})




