import dayjs from 'dayjs'

/**
 * 格式化日期时间
 * @param {string|Date} date 
 * @param {string} format 
 * @returns {string}
 */
export const formatDate = (date, format = 'YYYY-MM-DD') => {
  if (!date) return ''
  return dayjs(date).format(format)
}

/**
 * 格式化日期时间
 * @param {string|Date} datetime 
 * @returns {string}
 */
export const formatDateTime = (datetime) => {
  return formatDate(datetime, 'YYYY-MM-DD HH:mm:ss')
}

/**
 * 格式化时间
 * @param {string|Date} time 
 * @returns {string}
 */
export const formatTime = (time) => {
  return formatDate(time, 'HH:mm:ss')
}

/**
 * 格式化文件大小
 * @param {number} size 字节数
 * @returns {string}
 */
export const formatFileSize = (size) => {
  if (!size) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  let index = 0
  while (size >= 1024 && index < units.length - 1) {
    size /= 1024
    index++
  }
  return `${size.toFixed(2)} ${units[index]}`
}

/**
 * 脱敏手机号
 * @param {string} phone 
 * @returns {string}
 */
export const maskPhone = (phone) => {
  if (!phone) return ''
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

/**
 * 脱敏姓名
 * @param {string} name 
 * @returns {string}
 */
export const maskName = (name) => {
  if (!name) return ''
  if (name.length === 2) {
    return name.substring(0, 1) + '*'
  }
  return name.substring(0, 1) + '*'.repeat(name.length - 2) + name.substring(name.length - 1)
}

/**
 * 脱敏身份证号
 * @param {string} idCard 
 * @returns {string}
 */
export const maskIdCard = (idCard) => {
  if (!idCard) return ''
  return idCard.replace(/(\d{6})\d{8}(\d{4})/, '$1********$2')
}

/**
 * 获取时间问候语
 * @returns {string}
 */
export const getGreeting = () => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 17) return '下午好'
  if (hour < 19) return '傍晚好'
  if (hour < 22) return '晚上好'
  return '夜深了'
}




