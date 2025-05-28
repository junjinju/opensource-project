<template>
  <div class="ocr-detail-page">
    <h1>Parsed Receipt Details</h1>
    <p v-if="loading">Loading parsed items...</p>
    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="parsedItems.length" class="item-form">
      <form @submit.prevent="submitItems">
        <div v-for="(item, index) in parsedItems" :key="index" class="item-row">
          <input v-model="item.name" placeholder="Item Name" required />
          <input v-model="item.quantity" placeholder="Quantity" required />
          <select v-model="item.inventory_id">
            <option disabled value="">-- Select Inventory --</option>
            <option v-for="inv in inventories" :key="inv.id" :value="inv.id">
              {{ inv.name }}
            </option>
          </select>
          <select v-model="item.storage_type">
            <option value="fridge">Fridge</option>
            <option value="freezer">Freezer</option>
          </select>
          <input
            v-model="item.expiration_date"
            type="date"
            placeholder="Expiration Date"
            required
          />
        </div>

        <button type="submit">Save Selected Items</button>
      </form>
    </div>
  </div>
</template>

<script>
import { getParsedItems, deleteParsedItems, saveParsedItems } from '@/api/receipt'
import { getInventories } from '@/api/inventoires'

export default {
  name: 'OCRDetailPage',
  data() {
    return {
      parsedItems: [],
      inventories: [],
      loading: true,
      error: null,
    }
  },
  async mounted() {
    const receiptId = this.$route.params.id
    try {
      const [itemsRes, invRes] = await Promise.all([getParsedItems(receiptId), getInventories()])
      this.inventories = invRes.data
      this.parsedItems = itemsRes.data.map((item) => ({
        name: item.name,
        quantity: item.quantity,
        inventory_id: '',
        storage_type: 'fridge',
        expiration_date: '',
      }))
    } catch (err) {
      this.error = 'Failed to load parsed items or inventories.'
      console.error(err)
    } finally {
      this.loading = false
    }
  },
  methods: {
    async submitItems() {
      const receiptId = this.$route.params.id
      try {
        await saveParsedItems({
          receipt_id: receiptId,
          items: this.parsedItems,
        })
        await deleteParsedItems(receiptId)
        alert('Items saved and parsed data cleared.')
        this.$router.push('/inventory')
      } catch (err) {
        console.error('Save failed:', err)
        alert('Failed to save items.')
      }
    },
  },
}
</script>

<style scoped>
.ocr-detail-page {
  padding: 2rem;
  font-family: Arial, sans-serif;
}
.item-form {
  margin-top: 1rem;
}
.item-row {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}
.item-row input,
.item-row select {
  padding: 0.5rem;
  flex: 1;
  min-width: 150px;
}
button {
  padding: 0.6rem 1.2rem;
  background: #000;
  color: #fff;
  border: none;
  cursor: pointer;
}
.error {
  color: red;
  margin-top: 1rem;
}
</style>
