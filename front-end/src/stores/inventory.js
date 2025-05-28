import { defineStore } from 'pinia'
import api from '@/lib/api'

export const useInventoryStore = defineStore('inventory', {
  state: () => ({
    inventories: [],
  }),
  actions: {
    async fetchInventories() {
      const res = await api.get('/inventories')
      this.inventories = res.data
    },
    async createInventory(name) {
      const res = await api.post('/inventories', { name })
      this.inventories.push(res.data)
    },
  },
})
