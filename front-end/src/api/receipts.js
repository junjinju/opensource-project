// src/api/receipts.js
// 📦 영수증 관련 API 함수 모음
import api from './index'

// ✅ 영수증 이미지 업로드 (OCR)
export const uploadReceipt = (formData) => {
  return api.post('/receipts', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}

// ✅ 최근 업로드된 영수증 목록 불러오기 (필요시 사용)
export const getRecentReceipts = () => {
  return api.get('/receipt/recent')
}

// ✅ 특정 영수증의 OCR 결과 조회
export const getParsedItemsByReceiptId = (receiptId) => {
  return api.get(`/receipts/${receiptId}/parsed-items`)
}

// ✅ 특정 영수증의 ParsedItem 삭제 요청
export const deleteParsedItems = (receiptId) => {
  return api.post(`/parseditem/delete/${receiptId}`)
}
