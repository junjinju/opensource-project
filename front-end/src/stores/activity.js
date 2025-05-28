import { defineStore } from 'pinia'

export const useActivityStore = defineStore('activity', {
  state: () => ({
    logs: [],
  }),
  actions: {
    addLog(type, name, qty) {
      this.logs.unshift({
        type,
        name,
        qty,
        timestamp: new Date(),
      })
    },
  },
  getters: {
    recentLogs: (state) => {
      return state.logs.slice(0, 5)
    },
  },
})
