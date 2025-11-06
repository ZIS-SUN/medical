import { defineStore } from 'pinia'

export const useAppointmentStore = defineStore('appointment', {
  state: () => ({
    currentAppointment: null,
    selectedDoctor: null,
    selectedSchedule: null
  }),

  actions: {
    setCurrentAppointment(appointment) {
      this.currentAppointment = appointment
    },

    setSelectedDoctor(doctor) {
      this.selectedDoctor = doctor
    },

    setSelectedSchedule(schedule) {
      this.selectedSchedule = schedule
    },

    clearAppointmentData() {
      this.currentAppointment = null
      this.selectedDoctor = null
      this.selectedSchedule = null
    }
  }
})




