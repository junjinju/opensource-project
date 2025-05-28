// src/api/index.ts
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://100.76.109.125:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')


    // ❗ 인증 필요 없는 경로는 제외
    const skipAuthPaths = ['/auth/login', '/auth/signup']
    if (token && !skipAuthPaths.includes(config.url)) {
      config.headers.Authorization = `Bearer ${token}`
    }

    // ✅ 로그는 여기서 함께 찍으면 됩니다
    console.log('[🔍 최종 Request Headers]', config.headers)

    return config
  },
  (error) => Promise.reject(error)
)

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      alert('인증이 만료되었습니다. 다시 로그인해주세요.')
      window.location.href = '/'
    }
    return Promise.reject(error)
  }
)

export default api
