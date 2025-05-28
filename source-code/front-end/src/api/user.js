// 사용자 관련 API 함수 모음
import api from './index'

// ✅ 로그인
export const loginUser = (loginData) => {
  return api.post('/user/login', loginData)
}

// ✅ 회원가입 (선택적)
export const registerUser = (registerData) => {
  return api.post('/user/register', registerData)
}

// ✅ 사용자 정보 가져오기
export const getUserInfo = (userId) => {
  return api.get(`/user/${userId}`)
}

// ✅ 사용자 정보 업데이트
export const updateUser = (userId, updatedData) => {
  return api.put(`/user/${userId}`, updatedData)
}

export const updatePassword = (userId, newPassword) => {
  return api.put(`/user/${userId}/password`, { password: newPassword })
}

