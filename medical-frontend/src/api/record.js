import request from '@/utils/request'

/**
 * 获取我的病历列表
 */
export const getMyRecords = (params) => {
  return request({
    url: '/api/records/my',
    method: 'get',
    params
  })
}

/**
 * 获取病历详情
 */
export const getRecordDetail = (id) => {
  return request({
    url: `/api/records/${id}`,
    method: 'get'
  })
}

/**
 * 管理端获取病历列表
 */
export const getRecordList = (params) => {
  return request({
    url: '/api/admin/records',
    method: 'get',
    params
  })
}

/**
 * 创建病历
 */
export const createRecord = (data) => {
  return request({
    url: '/api/admin/records',
    method: 'post',
    data
  })
}

/**
 * 更新病历
 */
export const updateRecord = (id, data) => {
  return request({
    url: `/api/admin/records/${id}`,
    method: 'put',
    data
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




