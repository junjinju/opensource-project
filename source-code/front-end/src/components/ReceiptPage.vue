<template>
  <div class="receipt-page">
    <h2 class="title">Upload Receipt for OCR</h2>
    <p class="subtitle">Drag or select a receipt image to upload and perform OCR.</p>

    <div class="upload-area">
      <div class="upload-box">
        <label>📷 Upload Receipt Image</label>
        <input type="file" accept="image/*" @change="handleFileChange" />

        <div v-if="imagePreview" class="drop-zone">
          <img :src="imagePreview" class="preview" alt="Receipt Preview" />
          <p>Do you want to proceed with OCR?</p>
          <button @click="submitImage" :disabled="isLoading">✅ Yes</button>
          <button @click="resetForm" :disabled="isLoading">❌ No</button>
        </div>
      </div>

      <div class="guide-box">
        <h3>📌 Guide</h3>
        <img src="@/assets/OCR_Guide.png" class="guide-image" alt="Guide" />
        <p class="guide-text">
          - Please upload a clearly photographed receipt image.<br />
          - After OCR, review and edit the extracted item information.<br />
          - After editing, the items will be automatically added to your inventory.
        </p>
      </div>
    </div>

    <!-- Loading Message -->
    <div v-if="isLoading" class="loading-overlay">
      <div class="loading-message">Please wait... OCR is being processed.</div>
    </div>

    <!-- OCR Result Modal -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h3>🧾 Review Extracted Items</h3>
        <div v-for="(item, idx) in parsedItems" :key="idx" class="item-row">
          <input v-model="item.item_name" placeholder="Item name" />
          <input type="number" min="1" v-model.number="item.quantity" placeholder="Quantity" />

          <select v-model="item.storage_type">
            <option value="">Select storage type</option>
            <option value="fridge">Fridge</option>
            <option value="freezer">Freezer</option>
          </select>

          <input type="date" v-model="item.expiration_date" placeholder="Expiration date" />

          <select v-model="item.inventory_id">
            <option disabled value="">Select inventory</option>
            <option v-for="inv in inventories" :key="inv.inventory_id" :value="inv.inventory_id">
              {{ inv.inventory_name }}
            </option>
          </select>

          <button @click="removeItem(idx)">🗑</button>
        </div>

        <div class="modal-actions">
          <button @click="saveItems" :disabled="isSaving">💾 Save</button>
          <button @click="closeModal" :disabled="isSaving">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { uploadReceipt, getParsedItemsByReceiptId, deleteParsedItems } from '@/api/receipts'
import { addInventoryItem, getInventoryList } from '@/api/inventory'

export default {
  name: 'OCRUpload',
  data() {
    return {
      imageFile: null,
      imagePreview: '',
      receiptId: null,
      parsedItems: [],
      inventories: [],
      showModal: false,
      isLoading: false,
      isSaving: false,
    }
  },
  methods: {
    handleFileChange(e) {
      const file = e.target.files[0]
      if (file) {
        this.imageFile = file
        this.imagePreview = URL.createObjectURL(file)
      }
    },
    async submitImage() {
      if (!this.imageFile) return
      this.isLoading = true
      try {
        const formData = new FormData()
        formData.append('image', this.imageFile)

        const res = await uploadReceipt(formData)
        const receiptId = res.data.receiptId

        if (!receiptId) {
          throw new Error('OCR failed or response error: missing receiptId.')
        }

        this.receiptId = receiptId

        const parsed = await getParsedItemsByReceiptId(receiptId)
        this.parsedItems = parsed.data.map((item) => ({
          item_name: item.itemName,
          quantity: item.quantity,
          storage_type: '',
          expiration_date: '',
          inventory_id: null,
        }))

        const invRes = await getInventoryList()
        this.inventories = (invRes.data || []).map((inv) => ({
          inventory_id: inv.inventoryId,
          inventory_name: inv.inventoryName,
        }))

        this.showModal = true
      } catch (err) {
        console.error('OCR or fetch failed:', err)
        alert('An error occurred during OCR processing.')
      } finally {
        this.isLoading = false
      }
    },
    async saveItems() {
      this.isSaving = true
      try {
        const savePromises = this.parsedItems.map((item) => {
          const payload = {
            inventoryId: item.inventory_id,
            itemName: item.item_name,
            quantity: Number(item.quantity),
            storageType: item.storage_type,
            expirationDate: item.expiration_date,
          }
          return addInventoryItem(payload)
        })

        await Promise.all(savePromises)
        // await deleteParsedItems(this.receiptId)
        this.resetForm()
        alert('Save completed successfully!')
      } catch (err) {
        console.error('Save failed:', err)
        alert('An error occurred while saving.')
      } finally {
        this.isSaving = false
      }
    },
    removeItem(index) {
      this.parsedItems.splice(index, 1)
    },
    closeModal() {
      this.showModal = false
    },
    resetForm() {
      this.imageFile = null
      this.imagePreview = ''
      this.receiptId = null
      this.parsedItems = []
      this.showModal = false
    },
  },
}
</script>

<style scoped>
.receipt-page {
  padding: 2rem;
  font-family: sans-serif;
}
.title {
  font-size: 2rem;
  font-weight: bold;
}
.subtitle {
  margin-bottom: 2rem;
  color: #777;
}
.upload-area {
  display: flex;
  gap: 2rem;
  margin-bottom: 3rem;
  flex-wrap: wrap;
}
.upload-box {
  flex: 1;
  border: 2px dashed #ccc;
  padding: 1.5rem;
  border-radius: 8px;
  text-align: center;
  min-width: 300px;
}
.drop-zone {
  margin: 1rem 0;
  padding: 1rem;
  border: 1px dashed #aaa;
  color: #666;
  text-align: center;
}
.guide-box {
  flex: 1;
  background: #f9f9f9;
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid #ccc;
  min-width: 300px;
}
.guide-image {
  width: 100%;
  border-radius: 6px;
  margin: 1rem 0;
}
.guide-text {
  font-size: 0.9rem;
  color: #444;
}
.preview {
  max-width: 300px;
  margin: 1rem auto;
  border: 1px solid #ccc;
}
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  width: 600px;
  max-height: 80vh;
  overflow-y: auto;
}
.item-row {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
  align-items: center;
}
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.loading-message {
  font-size: 1.2rem;
  color: #333;
}
button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
