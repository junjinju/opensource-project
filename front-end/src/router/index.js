import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../components/LoginPage.vue'
import HomePage from '../components/HomePage.vue'
import ReceiptPage from '../components/ReceiptPage.vue'
import InventoryPage from '../components/InventoryPage.vue'
import AccountPage from '../components/AccountPage.vue'
import RecomPage from '../components/RecomPage.vue'

const routes = [
  { path: '/', component: LoginPage },
  { path: '/home', component: HomePage },
  { path: '/receipt', component: ReceiptPage },
  { path: '/inventory', component: InventoryPage },
  { path: '/account', component: AccountPage },
  { path: '/inventory/:id', component: InventoryPage },
  {
    path: '/oauth/kakao/callback',
    name: 'KakaoCallback',
    component: () => import('@/pages/KakaoCallback.vue'),
  },
  { path: '/recipes', component: RecomPage },
  {
    path: '/receipt/:id',
    component: () => import('@/components/OCRDetailPage.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
