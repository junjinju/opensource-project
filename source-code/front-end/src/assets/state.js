import { reactive } from 'vue'

export const activityLog = reactive([])
export const storageSections = reactive([
  {
    name: 'Refrigerated',
    categories: [
      {
        name: 'Dairy',
        items: [
          { name: 'Milk', qty: '2L', expiry: '2025-06-10' },
          { name: 'Cheese', qty: '500g', expiry: '2025-08-20' },
        ],
        count: 2,
      },
      {
        name: 'Vegetables',
        items: [
          { name: 'Tomatoes', qty: '100g', expiry: '2025-06-12' },
          { name: 'Lettuce', qty: '2 heads', expiry: '2025-06-13' },
        ],
        count: 2,
      },
    ],
  },
  {
    name: 'Frozen',
    categories: [
      {
        name: 'Ice Cream',
        items: [{ name: 'Vanilla', qty: '1L', expiry: '2026-08-01' }],
        count: 1,
      },
      {
        name: 'Peas',
        items: [{ name: 'Green Peas', qty: '500g', expiry: '2025-08-01' }],
        count: 1,
      },
    ],
  },
])
