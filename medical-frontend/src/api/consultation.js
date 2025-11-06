import request from '@/utils/request'

/**
 * 创建问诊
 */
export const createConsultation = (data) => {
  return request({
    url: '/api/consultations',
    method: 'post',
    data
  })
}

/**
 * 获取我的问诊列表
 */
export const getMyConsultations = (params) => {
  return request({
    url: '/api/consultations/my',
    method: 'get',
    params
  })
}

/**
 * 获取问诊详情
 */
export const getConsultationDetail = (id) => {
  return request({
    url: `/api/consultations/${id}`,
    method: 'get'
  })
}

/**
 * 管理端获取问诊列表
 */
export const getConsultationList = (params) => {
  return request({
    url: '/api/admin/consultations',
    method: 'get',
    params
  })
}

/**
 * 医生回复问诊
 */
export const replyConsultation = (id, data) => {
  return request({
    url: `/api/admin/consultations/${id}/reply`,
    method: 'post',
    data
  })
}

/**
 * 关闭问诊
 */
export const closeConsultation = (id) => {
  return request({
    url: `/api/admin/consultations/${id}/close`,
    method: 'put'
  })
}




