import { defineStore } from 'pinia'
import { getDepartments } from '@/api/department'

export const useDepartmentStore = defineStore('department', {
  state: () => ({
    departments: [],
    loading: false
  }),

  getters: {
    departmentMap: (state) => {
      const map = {}
      state.departments.forEach(dept => {
        map[dept.id] = dept
      })
      return map
    },
    getDepartmentName: (state) => (id) => {
      const dept = state.departments.find(d => d.id === id)
      return dept ? dept.name : ''
    }
  },

  actions: {
    async fetchDepartments() {
      if (this.departments.length > 0) {
        return this.departments
      }
      
      this.loading = true
      try {
        const data = await getDepartments()
        this.departments = data
        return data
      } finally {
        this.loading = false
      }
    }
  }
})




