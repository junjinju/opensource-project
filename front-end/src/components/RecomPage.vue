<template>
  <div class="inventory-page">
    <h1 class="text-2xl font-bold mb-2">AI Recipe Recommendations</h1>
    <p class="subtitle">You can generate recipes based on your stock!</p>

    <!-- Inventory 선택 드롭다운 -->
    <label class="inventory-label">
      Select Inventory:
      <select v-model="selectedInventoryId" class="inventory-dropdown">
        <option disabled value="">-- Please choose --</option>
        <option v-for="inv in inventoryList" :key="inv.inventoryId" :value="inv.inventoryId">
          {{ inv.inventoryName }}
        </option>
      </select>
    </label>

    <button class="manage-btn mb-4" @click="openItemModal">🧂 Select Items for Recipe</button>

    <!-- 레시피 생성 모달 -->
    <div v-if="showItemModal" class="manage-modal" @click.self="closeItemModal">
      <div class="modal-content">
        <h2 class="text-xl font-bold mb-2">Select Items for Recipe</h2>
        <ul class="list-disc list-inside">
          <li v-for="item in inventoryItems" :key="item.itemId">
            <label>
              <input type="checkbox" v-model="selectedItems" :value="item" />
              {{ item.itemName }} (x{{ item.quantity }})
            </label>
          </li>
        </ul>
        <div class="modal-actions mt-4">
          <button class="manage-btn mr-2" @click="submitRecipe">Submit</button>
          <button class="manage-btn" @click="closeItemModal">Cancel</button>
        </div>
      </div>
    </div>

    <!-- 생성된 레시피 목록 -->
    <h2 class="text-lg font-semibold mt-6 mb-2">📜 Created Recipes</h2>
    <ul class="recipe-list">
      <li
        v-for="recipe in recipes"
        :key="recipe.recipeId"
        class="recipe-card"
        @click="openRecipe(recipe.recipeId)"
      >
        <div class="title">{{ recipe.title }}</div>
        <div class="created">Created: {{ formatDate(recipe.createdAt) }}</div>
      </li>
    </ul>

    <!-- 레시피 상세 모달 -->
    <div v-if="selectedRecipe" class="manage-modal" @click.self="selectedRecipe = null">
      <div class="modal-content">
        <h2 class="text-xl font-bold mb-2">{{ selectedRecipe.title }}</h2>
        <p class="mb-3 whitespace-pre-line">{{ selectedRecipe.content }}</p>
        <div class="modal-actions mt-4">
          <button class="manage-btn mr-2" @click="selectedRecipe = null">Close</button>
          <button class="manage-btn delete-btn" @click="deleteRecipe(selectedRecipe.recipeId)">Delete</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getInventoryList, getInventoryItems } from '@/api/inventory'
import {
  createRecipeFromItems,
  getRecipeList,
  getRecipeById,
  deleteRecipeById
} from '@/api/recipe'

const selectedInventoryId = ref('')
const inventoryList = ref([])
const inventoryItems = ref([])
const selectedItems = ref([])
const recipes = ref([])
const selectedRecipe = ref(null)
const showItemModal = ref(false)

onMounted(() => {
  fetchInventoryList()
  fetchRecipeList()
})

async function fetchInventoryList() {
  try {
    const res = await getInventoryList()
    inventoryList.value = res.data
  } catch (e) {
    console.error('Inventory list fetch failed:', e)
  }
}

async function fetchInventoryItems() {
  if (!selectedInventoryId.value) return
  try {
    const res = await getInventoryItems(selectedInventoryId.value)
    inventoryItems.value = res.data
  } catch (e) {
    console.error('Inventory item fetch failed:', e)
  }
}

async function fetchRecipeList() {
  try {
    const res = await getRecipeList()
    recipes.value = res.data
  } catch (e) {
    console.error('Recipe list fetch failed:', e)
  }
}

function openItemModal() {
  if (!selectedInventoryId.value) {
    alert('Please select an inventory.')
    return
  }
  fetchInventoryItems()
  showItemModal.value = true
}

function closeItemModal() {
  showItemModal.value = false
  selectedItems.value = []
}

async function submitRecipe() {
  if (selectedItems.value.length === 0) {
    alert('Please select at least one item.')
    return
  }

  const payload = selectedItems.value.map(item => ({
    item_name: item.itemName,
    quantity: item.quantity
  }))

  try {
    await createRecipeFromItems(payload)
    closeItemModal()
    fetchRecipeList()
  } catch (e) {
    console.error('Recipe creation failed:', e)
  }
}

async function openRecipe(recipeId) {
  try {
    const res = await getRecipeById(recipeId)
    selectedRecipe.value = {
      recipeId: recipeId,
      title: res.data.title,
      content: res.data.content
    }
  } catch (e) {
    console.error('Failed to fetch recipe details:', e)
  }
}

async function deleteRecipe(recipeId) {
  const confirmed = confirm('Are you sure you want to delete this recipe?')
  if (!confirmed) return

  try {
    await deleteRecipeById(recipeId)
    selectedRecipe.value = null
    await fetchRecipeList()
  } catch (e) {
    console.error('Failed to delete recipe:', e)
    alert('Failed to delete recipe.')
  }
}

function formatDate(dateStr) {
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()}`
}
</script>

<style scoped>
@import '@/assets/recipe-page.css';

.inventory-page {
  padding: 2rem;
  font-family: Arial, sans-serif;
}
.subtitle {
  color: #666;
  margin-bottom: 1.5rem;
}
.manage-btn {
  background: #000;
  color: #fff;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}
.delete-btn {
  background-color: #e53935;
  color: white;
}
.delete-btn:hover {
  background-color: #c62828;
}
.inventory-label {
  font-weight: 600;
  display: block;
  margin-bottom: 1rem;
}
.inventory-dropdown {
  margin-top: 0.3rem;
  padding: 0.4rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 0.95rem;
}
.recipe-list {
  margin-top: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.recipe-card {
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}
.recipe-card:hover {
  background: #f9f9f9;
}
.title {
  font-weight: bold;
  font-size: 1.1rem;
}
.created {
  color: #999;
  font-size: 0.9rem;
  margin-top: 0.3rem;
}
.manage-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  width: 600px;
  max-height: 80vh;
  overflow-y: auto;
}
.modal-actions {
  text-align: right;
}
</style>