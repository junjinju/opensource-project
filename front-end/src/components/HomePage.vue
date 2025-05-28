<template>
  <div class="dashboard">
    <!-- 인벤토리 선택 -->
    <div class="inventory-select">
      <label for="inventory">Select Inventory:</label>
      <select
        id="inventory"
        v-model="selectedInventoryId"
        @change="fetchData"
      >
        <option disabled value="">-- 선택 --</option>
        <option
          v-for="inv in inventories"
          :key="inv.inventoryId"
          :value="inv.inventoryId"
        >
          {{ inv.inventoryName }}
        </option>
      </select>
    </div>

    <!-- 선택된 인벤토리 이름 -->
    <div class="inventory-header">
      <h2>{{ currentInventoryName }}</h2>
    </div>

    <!-- 카드 영역 -->
    <section class="cards">
      <!-- Expiring Soon -->
      <div class="card expiring">
        <h3>Items Expiring Soon</h3>
        <span class="badge red">{{ expiringItems.length }} Items</span>
        <ul>
          <li v-for="item in expiringItems" :key="item.itemId">
            {{ item.itemName }} – {{ item.expirationDate }}
          </li>
        </ul>
      </div>

      <!-- Low Stock -->
      <div class="card low-stock">
        <h3>Low Stock Items</h3>
        <span class="badge black">{{ lowStockItems.length }} Items</span>
        <ul>
          <li v-for="item in lowStockItems" :key="item.itemId">
            {{ item.itemName }} – {{ item.quantity }} left
          </li>
        </ul>
      </div>

      <!-- Recipe Suggestions -->
      <div class="card suggestions">
        <h3>Recipe Suggestions</h3>
        <ul>
          <li v-for="rec in recipeSuggestions" :key="rec.recipeId">
            {{ rec.title }} <small>({{ rec.matchPercent }}% match)</small>
          </li>
        </ul>
      </div>

      <!-- Quick Actions -->
      <div class="card actions">
        <h3>Quick Actions</h3>
        <button class="action-btn primary" @click="showAddForm = true">
          Add Item
        </button>
        <button class="action-btn" @click="$router.push('/receipt')">
          Scan Receipt
        </button>
        <button class="action-btn" @click="$router.push('/recipes')">
          Find Recipes
        </button>
        <button class="action-btn" @click="showManageItems = !showManageItems">
          {{ showManageItems ? 'Hide Management' : 'Edit Items' }}
        </button>

        <!-- Add Item Form -->
        <div v-if="showAddForm" class="add-item-form">
          <label>
            Storage:
            <select v-model="newItem.storageType">
              <option disabled value="">-- 선택 --</option>
              <option value="fridge">Fridge</option>
              <option value="freezer">Freezer</option>
            </select>
          </label>
          <label>
            Name:
            <input v-model="newItem.itemName" placeholder="Item name" />
          </label>
          <label>
            Quantity:
            <input v-model.number="newItem.quantity" type="number" min="1" />
          </label>
          <label>
            Expiration Date:
            <input v-model="newItem.expirationDate" type="date" />
          </label>
          <div class="form-actions">
            <button @click="addItem">Submit</button>
            <button @click="cancelAdd">Cancel</button>
          </div>
        </div>

        <!-- Manage Items List -->
        <div v-if="showManageItems" class="manage-items">
          <ul>
            <li v-for="item in allItems" :key="item.itemId" class="manage-item">
              <span>
                {{ item.itemName }} ({{ item.quantity }}) - {{ item.expirationDate }}
              </span>
              <button @click="openEditModal(item)">Edit</button>
              <button @click="deleteItem(item.itemId)">Delete</button>
            </li>
          </ul>
        </div>
      </div>
    </section>

    <!-- Overview & Charts -->
    <section class="overview">
      <div class="inventory-overview">
        <h3>Inventory Overview</h3>
        <div class="donut-container">
          <div class="donut-box">
            <h4>Fridge</h4>
            <Doughnut :data="fridgeChartData" :options="chartOptions" />
          </div>
          <div class="donut-box">
            <h4>Freezer</h4>
            <Doughnut :data="freezerChartData" :options="chartOptions" />
          </div>
        </div>
      </div>
    </section>

    <!-- Edit Item Modal -->
    <div v-if="showEditModal" class="edit-modal">
      <div class="modal-content">
        <h3>Edit Item</h3>
        <label>
          Name:
          <input v-model="editItem.itemName" placeholder="Name" />
        </label>
        <label>
          Quantity:
          <input v-model.number="editItem.quantity" type="number" placeholder="Quantity" />
        </label>
        <label>
          Expiration Date:
          <input v-model="editItem.expirationDate" type="date" placeholder="Expiration Date" />
        </label>
        <label>
          Storage:
          <select v-model="editItem.storageType">
            <option value="fridge">Fridge</option>
            <option value="freezer">Freezer</option>
          </select>
        </label>
        <div class="modal-actions">
          <button @click="updateItem">Save</button>
          <button @click="closeEditModal">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Doughnut } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, CategoryScale } from 'chart.js'
import {
  getInventoryList,
  getInventoryItems,
  addInventoryItem,
  updateInventoryItem,
  deleteInventoryItem
} from '@/api/inventory'
import * as recipeAPI from '@/api/recipe'

ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale)

export default {
  name: 'HomePage',
  components: { Doughnut },
  data() {
    return {
      inventories: [],
      selectedInventoryId: this.$route.query.id || '',
      allItems: [],
      recipeSuggestions: [],
      showAddForm: false,
      showManageItems: false,
      showEditModal: false,
      newItem: { storageType: '', itemName: '', quantity: 1, expirationDate: '' },
      editItem: {},
      fridgeColors: {},
      freezerColors: {},
    }
  },
  computed: {
    currentInventoryName() {
      const inv = this.inventories.find(i => i.inventoryId === this.selectedInventoryId)
      return inv ? inv.inventoryName : 'No Inventory'
    },
    expiringItems() {
      const today = new Date()
      return this.allItems
        .filter(item => item.expirationDate)
        .filter(item => {
          const diff = (new Date(item.expirationDate) - today) / (1000*60*60*24)
          return diff >= 0 && diff <= 5
        })
        .sort((a,b) => new Date(a.expirationDate) - new Date(b.expirationDate))
    },
    lowStockItems() {
      return this.allItems.filter(item => Number(item.quantity) <= 2)
    },
    fridgeItems() {
      return this.allItems.filter(item => item.storageType === 'fridge')
    },
    freezerItems() {
      return this.allItems.filter(item => item.storageType === 'freezer')
    },
    fridgeChartData() {
      const labels = [], data = [], bg = []
      this.fridgeItems.forEach(item => {
        labels.push(item.itemName)
        data.push(Number(item.quantity))
        if (!this.fridgeColors[item.itemId]) {
          this.fridgeColors[item.itemId] =
            '#' + Math.floor(Math.random() * 0xffffff)
              .toString(16)
              .padStart(6, '0')
        }
        bg.push(this.fridgeColors[item.itemId])
      })
      return { labels, datasets: [{ data, backgroundColor: bg, borderWidth: 1 }] }
    },
    freezerChartData() {
      const labels = [], data = [], bg = []
      this.freezerItems.forEach(item => {
        labels.push(item.itemName)
        data.push(Number(item.quantity))
        if (!this.freezerColors[item.itemId]) {
          this.freezerColors[item.itemId] =
            '#' + Math.floor(Math.random() * 0xffffff)
              .toString(16)
              .padStart(6, '0')
        }
        bg.push(this.freezerColors[item.itemId])
      })
      return { labels, datasets: [{ data, backgroundColor: bg, borderWidth: 1 }] }
    },
    chartOptions() {
      return { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom' } } }
    }
  },
  methods: {
    async fetchInventories() {
      try {
        const { data } = await getInventoryList()
        this.inventories = data.map(i => ({ inventoryId: i.inventoryId, inventoryName: i.inventoryName }))
        this.selectedInventoryId = this.$route.query.id || (this.inventories[0] && this.inventories[0].inventoryId)
        await this.fetchData()
      } catch (e) { console.error(e) }
    },
    async fetchData() {
      try {
        const { data: items } = await getInventoryItems(this.selectedInventoryId)
        this.allItems = items.map(it => ({
          itemId: it.itemId,
          itemName: it.itemName,
          quantity: it.quantity,
          storageType: it.storageType,
          expirationDate: it.expirationDate
        }))
        if (!this.fridgeColors.length) {
          this.fridgeColors = this.fridgeItems.map(() =>
            '#' + Math.floor(Math.random() * 0xffffff).toString(16).padStart(6, '0')
          )
        }
        if (!this.freezerColors.length) {
          this.freezerColors = this.freezerItems.map(() =>
            '#' + Math.floor(Math.random() * 0xffffff).toString(16).padStart(6, '0')
          )
        }
        const { data: recs } = await getRecommendedRecipes(this.selectedInventoryId)
        this.recipeSuggestions = recs.map(r => ({ recipeId: r.recipeId, title: r.title, matchPercent: r.matchPercent }))
      } catch (e) { console.error(e) }
    },
    cancelAdd() {
      this.showAddForm = false
      this.newItem = { storageType: '', itemName: '', quantity: 1, expirationDate: '' }
    },
    async addItem() {
      if (!this.selectedInventoryId || !this.newItem.storageType || !this.newItem.itemName) return
      try {
        const payload = {
          inventoryId: this.selectedInventoryId,
          itemName: this.newItem.itemName,
          quantity: this.newItem.quantity,
          storageType: this.newItem.storageType,
          expirationDate: this.newItem.expirationDate
        }

        const { data } = await addInventoryItem(payload)
        this.allItems.push(data)
        this.cancelAdd()
      } catch (e) { console.error(e) }
    },
    async deleteItem(id) {
      try {
        await deleteInventoryItem(id)
        this.allItems = this.allItems.filter(i => i.itemId !== id)
      } catch (e) { console.error(e) }
    },
    openEditModal(item) {
      this.editItem = { ...item }
      this.showEditModal = true
    },
    closeEditModal() {
      this.showEditModal = false
      this.editItem = {}
    },
    async updateItem() {
      try {
        const { data } = await updateInventoryItem(this.editItem.itemId, this.editItem)
        const idx = this.allItems.findIndex(i => i.itemId === this.editItem.itemId)
        if (idx !== -1) this.allItems.splice(idx, 1, data)
        this.closeEditModal()
      } catch (e) { console.error(e) }
    }
  },
  mounted() {
    this.fetchInventories()
  }
}//커밋 테스트
</script>

<style scoped>
.dashboard { padding: 2rem; font-family: Arial, sans-serif; }
.inventory-select { margin-bottom: 1rem; }
.inventory-header { margin-bottom: 2rem; }
.cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 1rem; margin-bottom: 2rem; }
.card { background: #fff; padding: 1rem; border-radius: 6px; box-shadow: 0 1px 4px rgba(0,0,0,0.1); min-height: 250px; }
.card h3 { margin-top: 0; }
.card.actions { display: flex; flex-direction: column; gap: 0.75rem; }
.action-btn { padding: 0.75rem 1rem; border: 1px solid #ccc; border-radius: 6px; cursor: pointer; }
.action-btn.primary { background: #000; color: #fff; border: none; }
.add-item-form, .manage-items, .edit-modal { margin-top: 1rem; }
.manage-items ul { list-style: none; padding: 0; }
.manage-item { display: flex; justify-content: space-between; margin-bottom: 0.5rem; }
.overview { display: flex; gap: 2rem; }
.donut-container { display: flex; gap: 1rem; }
.donut-box { width: 350px; height: 350px; position: relative; }
.edit-modal { position: fixed; top:0; left:0; width:100vw; height:100vh; background: rgba(0,0,0,0.5); display:flex; justify-content:center; align-items:center; }
.modal-content { background:#fff; padding:2rem; border-radius:8px; width:300px; }
.modal-actions { display:flex; justify-content:flex-end; gap:0.5rem; margin-top:1rem; }
</style>
