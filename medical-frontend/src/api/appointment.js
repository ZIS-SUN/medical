import request from '@/utils/request'

/**
 * 创建预约
 */
export const createAppointment = (data) => {
  return request({
    url: '/api/appointments',
    method: 'post',
    data
  })
}

/**
 * 获取我的预约列表
 */
export const getMyAppointments = (params) => {
  return request({
    url: '/api/appointments/my',
    method: 'get',
    params
  })
}

/**
 * 获取预约详情
 */
export const getAppointmentDetail = (id) => {
  return request({
    url: `/api/appointments/${id}`,
    method: 'get'
  })
}

/**
 * 取消预约
 */
export const cancelAppointment = (id, data) => {
  return request({
    url: `/api/appointments/${id}/cancel`,
    method: 'put',
    data
  })
}

/**
 * 管理端获取预约列表
 */
export const getAppointmentList = (params) => {
  return request({
    url: '/api/admin/appointments',
    method: 'get',
    params
  })
}

/**
 * 确认预约
 */
export const confirmAppointment = (id) => {
  return request({
    url: `/api/admin/appointments/${id}/confirm`,
    method: 'put'
  })
}

/**
 * 完成预约
 */
export const completeAppointment = (id) => {
  return request({
    url: `/api/admin/appointments/${id}/complete`,
    method: 'put'
  })
}




