import request from '@/utils/request'

/**
 * 获取公告列表
 */
export const getAnnouncements = (params) => {
  return request({
    url: '/api/announcements',
    method: 'get',
    params
  })
}

/**
 * 获取公告详情
 */
export const getAnnouncementDetail = (id) => {
  return request({
    url: `/api/announcements/${id}`,
    method: 'get'
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




