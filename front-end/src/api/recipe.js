// src/api/recipe.js
import api from './index'

// ✅ 선택한 재료 기반으로 레시피 생성
export const createRecipeFromItems = (payload) => {
  return api.post('/recipes', payload)
}

// ✅ 생성된 레시피 목록 조회
export const getRecipeList = () => {
  return api.get('/recipes')
}

// ✅ 특정 레시피 상세 정보 조회
export const getRecipeById = (recipeId) => {
  return api.get(`/recipes/${recipeId}`)
}

// ✅ 레시피 삭제
export const deleteRecipeById = (recipeId) => api.delete(`/recipes/${recipeId}`)