<template>
  <div class="inventory-page">
    <h1>Inventory Management</h1>
    <p class="subtitle">Select an inventory to view its dashboard</p>

    <div class="add-inventory-container">
      <input v-model="newInventoryName" placeholder="New Inventory Name" class="inventory-input" />
      <button @click="createInventory" class="add-inventory-btn">+ Add Inventory</button>
    </div>

    <div class="inventory-grid" v-if="inventories.length">
      <div
        v-for="inv in inventories"
        :key="inv.inventory_id"
        class="inventory-card-box"
      >
        <div v-if="editMode === inv.inventory_id">
          <input v-model="editName" class="inventory-input" />
          <button @click.stop="saveInventoryName(inv.inventory_id)" class="action-btn">Save</button>
          <button @click.stop="cancelEdit" class="action-btn">Cancel</button>
        </div>
        <div v-else @click="goToInventory(inv.inventory_id)">
          <h2 class="inventory-card-title">
            {{ inv.inventory_name || '(No Name)' }}
          </h2>
          <p class="inventory-count">{{ inv.itemCount }} items</p>
          <button @click.stop="startEdit(inv)" class="action-btn">Edit</button>
          <button @click.stop="deleteInventoryById(inv.inventory_id)" class="action-btn delete">Delete</button>
        </div>
      </div>
    </div>
    <p v-else class="no-inventories">아직 생성된 인벤토리가 없습니다.</p>
  </div>
</template>

<script>
import {
  getInventoryList,
  getInventoryItems,
  postInventory,
  updateInventory,
  deleteInventory
} from '@/api/inventory'

export default {
  name: 'InventoryPage',
  data() {
    return {
      inventories: [],
      newInventoryName: '',
      editMode: null,
      editName: ''
    }
  },
  methods: {
    async fetchInventories() {
      try {
        const { data: list } = await getInventoryList()
        this.inventories = await Promise.all(
          list.map(async (inv) => {
            const id = inv.inventory_id ?? inv.inventoryId ?? inv.id
            const inventoryName = inv.inventory_name ?? inv.inventoryName ?? inv.name

            let itemCount = 0
            try {
              const { data: items } = await getInventoryItems(id)
              itemCount = Array.isArray(items) ? items.length : 0
            } catch (e) {
              console.error(`Items fetch failed for ${id}:`, e)
            }

            return { inventory_id: id, inventory_name: inventoryName, itemCount }
          })
        )
      } catch (err) {
        console.error('Fetch inventories failed:', err)
      }
    },

    async createInventory() {
      const name = this.newInventoryName.trim()
      if (!name) return
      try {
        const { data } = await postInventory({ inventory_name: name })
        this.inventories.push({ inventory_id: data.inventory_id, inventory_name: name, itemCount: 0 })
        this.newInventoryName = ''
      } catch (err) {
        console.error('Create inventory failed:', err)
        alert('인벤토리 생성에 실패했습니다.')
      }
    },

    startEdit(inv) {
      this.editMode = inv.inventory_id
      this.editName = inv.inventory_name
    },

    cancelEdit() {
      this.editMode = null
      this.editName = ''
    },

    async saveInventoryName(inventoryId) {
      try {
        await updateInventory(inventoryId, { inventory_name: this.editName })
        const inv = this.inventories.find(i => i.inventory_id === inventoryId)
        if (inv) inv.inventory_name = this.editName
        this.cancelEdit()
      } catch (e) {
        console.error('Update inventory failed:', e)
      }
    },

    async deleteInventoryById(inventoryId) {
      if (!confirm('정말 삭제하시겠습니까?')) return
      try {
        await deleteInventory(inventoryId)
        this.inventories = this.inventories.filter(i => i.inventory_id !== inventoryId)
      } catch (e) {
        console.error('Delete inventory failed:', e)
      }
    },

    goToInventory(id) {
      this.$router.push({ path: '/home', query: { id } })
    }
  },
  mounted() {
    this.fetchInventories()
  }
}
</script>

<style scoped>
.inventory-page {
  padding: 2rem;
  font-family: Arial, sans-serif;
}
.subtitle {
  color: #666;
  margin-bottom: 1.5rem;
}
.add-inventory-container {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}
.inventory-input {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.add-inventory-btn {
  background: #000;
  color: #fff;
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.add-inventory-btn:hover {
  background: #333;
}
.inventory-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 1.5rem;
}
.inventory-card-box {
  background: #fff;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  text-align: center;
}
.inventory-card-box:hover {
  transform: translateY(-5px);
}
.inventory-card-title {
  margin: 0;
  font-size: 1.25rem;
}
.inventory-count {
  color: #555;
  margin-top: 0.5rem;
}
.no-inventories {
  text-align: center;
  color: #999;
  margin-top: 2rem;
}
.action-btn {
  margin-top: 0.5rem;
  margin-right: 0.5rem;
  padding: 0.4rem 0.8rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  background: #f8f8f8;
  cursor: pointer;
}
.action-btn.delete {
  background: #fdd;
  border-color: #f88;
  color: #800;
}
</style>