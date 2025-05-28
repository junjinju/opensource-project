import api from './index'

// ✅ 모든 인벤토리 조회
export const getInventoryList = () => api.get('/inventories')

// ✅ 인벤토리 생성
export const postInventory = (data) => api.post('/inventories', data)

// ✅ 인벤토리 이름 수정
export const updateInventory = (inventoryId, data) =>
  api.put(`/inventories/${inventoryId}`, data)

// ✅ 인벤토리 삭제
export const deleteInventory = (inventoryId) =>
  api.delete(`/inventories/${inventoryId}`)

// ✅ 해당 인벤토리의 모든 아이템 조회
export const getInventoryItems = (inventoryId) =>
  api.get(`/inventories/${inventoryId}/items`)

// ✅ 아이템 등록
export const addInventoryItem = (itemData) =>
  api.post('/inventory-items', itemData)  // 중요: inventoryS-itemS (복수)

// ✅ 아이템 수정
export const updateInventoryItem = (itemId, data) =>
  api.put(`/inventory-items/${itemId}`, data)

// ✅ 아이템 삭제
export const deleteInventoryItem = (itemId) =>
  api.delete(`/inventory-items/${itemId}`)

