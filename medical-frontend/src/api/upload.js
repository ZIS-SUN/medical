import request from '@/utils/request'

/**
 * 上传文件
 */
export const uploadFile = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/api/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传图片
 */
export const uploadImage = (file) => {
  return uploadFile(file)
}

/**
 * 获取上传地址
 */
export const getUploadUrl = () => {
  return import.meta.env.VITE_API_BASE_URL + '/api/upload'
}




