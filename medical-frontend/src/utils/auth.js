const TOKEN_KEY = 'medical_token'
const USER_INFO_KEY = 'medical_user_info'
const ADMIN_TOKEN_KEY = 'medical_admin_token'

export const getToken = () => {
  return localStorage.getItem(TOKEN_KEY)
}

export const setToken = (token) => {
  localStorage.setItem(TOKEN_KEY, token)
}

export const removeToken = () => {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_INFO_KEY)
}

export const getUserInfo = () => {
  const info = localStorage.getItem(USER_INFO_KEY)
  return info ? JSON.parse(info) : null
}

export const setUserInfo = (userInfo) => {
  localStorage.setItem(USER_INFO_KEY, JSON.stringify(userInfo))
}

export const getAdminToken = () => {
  return localStorage.getItem(ADMIN_TOKEN_KEY)
}

export const setAdminToken = (token) => {
  localStorage.setItem(ADMIN_TOKEN_KEY, token)
}

export const removeAdminToken = () => {
  localStorage.removeItem(ADMIN_TOKEN_KEY)
}




