import request from '@/utils/request'

/**
 * 获取数据看板统计
 */
export const getDashboardStats = () => {
  return request({
    url: '/api/admin/dashboard/stats',
    method: 'get'
  })
}

/**
 * 获取用户列表
 */
export const getUserList = (params) => {
  return request({
    url: '/api/admin/users',
    method: 'get',
    params
  })
}

/**
 * 禁用/启用用户
 */
export const toggleUserStatus = (id, status) => {
  return request({
    url: `/api/admin/users/${id}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * 获取排班列表
 */
export const getScheduleList = (params) => {
  return request({
    url: '/api/admin/schedules',
    method: 'get',
    params
  })
}

/**
 * 创建排班
 */
export const createSchedule = (data) => {
  return request({
    url: '/api/admin/schedules',
    method: 'post',
    data
  })
}

/**
 * 更新排班
 */
export const updateSchedule = (id, data) => {
  return request({
    url: `/api/admin/schedules/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除排班
 */
export const deleteSchedule = (id) => {
  return request({
    url: `/api/admin/schedules/${id}`,
    method: 'delete'
  })
}

/**
 * 批量创建排班
 */
export const batchCreateSchedules = (data) => {
  return request({
    url: '/api/admin/schedules/batch',
    method: 'post',
    data
  })
}

/**
 * 获取病历列表
 */
export const getRecordList = (params) => {
  return request({
    url: '/api/admin/records',
    method: 'get',
    params
  })
}

/**
 * 删除病历
 */
export const deleteRecord = (id) => {
  return request({
    url: `/api/admin/records/${id}`,
    method: 'delete'
  })
}

/**
 * 获取公告列表
 */
export const getAnnouncementList = (params) => {
  return request({
    url: '/api/admin/announcements',
    method: 'get',
    params
  })
}

/**
 * 创建公告
 */
export const createAnnouncement = (data) => {
  return request({
    url: '/api/admin/announcements',
    method: 'post',
    data
  })
}

/**
 * 更新公告
 */
export const updateAnnouncement = (id, data) => {
  return request({
    url: `/api/admin/announcements/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除公告
 */
export const deleteAnnouncement = (id) => {
  return request({
    url: `/api/admin/announcements/${id}`,
    method: 'delete'
  })
}

/**
 * 发布/取消发布公告
 */
export const toggleAnnouncementStatus = (id, status) => {
  return request({
    url: `/api/admin/announcements/${id}/status`,
    method: 'put',
    data: { status }
  })
}


