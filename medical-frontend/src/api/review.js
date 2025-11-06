import request from '@/utils/request'

/**
 * 创建评价
 */
export const createReview = (data) => {
  return request({
    url: '/api/reviews',
    method: 'post',
    data
  })
}

/**
 * 获取我的评价列表
 */
export const getMyReviews = (params) => {
  return request({
    url: '/api/reviews/my',
    method: 'get',
    params
  })
}

/**
 * 获取医生的评价列表
 */
export const getDoctorReviews = (doctorId, params) => {
  return request({
    url: `/api/reviews/doctor/${doctorId}`,
    method: 'get',
    params
  })
}




