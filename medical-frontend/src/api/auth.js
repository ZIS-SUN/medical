import request from '@/utils/request'

/**
 * 用户登录
 */
export const login = (data) => {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

/**
 * 用户注册
 */
export const register = (data) => {
  return request({
    url: '/api/auth/register',
    method: 'post',
    data
  })
}

/**
 * 获取当前用户信息
 */
export const getUserInfo = () => {
  return request({
    url: '/api/auth/info',
    method: 'get'
  })
}

/**
 * 管理员登录
 */
export const adminLogin = (data) => {
  return request({
    url: '/api/admin/auth/login',
    method: 'post',
    data
  })
}

/**
 * 修改密码
 */
export const updatePassword = (data) => {
  return request({
    url: '/api/auth/password',
    method: 'put',
    data
  })
}

/**
 * 更新个人信息
 */
export const updateProfile = (data) => {
  return request({
    url: '/api/auth/profile',
    method: 'put',
    data
  })
}

