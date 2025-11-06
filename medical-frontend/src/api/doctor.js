import request from '@/utils/request'

/**
 * 获取医生列表
 */
export const getDoctorList = (params) => {
  return request({
    url: '/api/doctors',
    method: 'get',
    params
  })
}

/**
 * 获取医生详情
 */
export const getDoctorDetail = (id) => {
  return request({
    url: `/api/doctors/${id}`,
    method: 'get'
  })
}

/**
 * 获取医生排班
 */
export const getDoctorSchedules = (id, params) => {
  return request({
    url: `/api/doctors/${id}/schedules`,
    method: 'get',
    params
  })
}

/**
 * 获取医生评价
 */
export const getDoctorReviews = (id, params) => {
  return request({
    url: `/api/doctors/${id}/reviews`,
    method: 'get',
    params
  })
}

/**
 * 获取热门医生
 */
export const getHotDoctors = () => {
  return request({
    url: '/api/doctors/hot',
    method: 'get'
  })
}

/**
 * 创建医生
 */
export const createDoctor = (data) => {
  return request({
    url: '/api/admin/doctors',
    method: 'post',
    data
  })
}

/**
 * 更新医生信息
 */
export const updateDoctor = (id, data) => {
  return request({
    url: `/api/admin/doctors/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除医生
 */
export const deleteDoctor = (id) => {
  return request({
    url: `/api/admin/doctors/${id}`,
    method: 'delete'
  })
}




