<script>
import { useRoute } from 'vue-router'

export default {
  setup() {
    const route = useRoute()
    const hiddenPaths = ['/']

    return { route, hiddenPaths }
  },
}
</script>

<template>
  <div id="app">
    <!-- 로그인/회원가입 페이지에서는 nav 숨김 -->
    <nav v-if="!hiddenPaths.includes(route.path)" class="navbar">
      <div class="nav-left">
        <img src="@/assets/logo.png" alt="Logo" class="logo" />
        <ul class="nav-links">
          <li><router-link to="/home">Dashboard</router-link></li>
          <li><router-link to="/inventory">Inventory</router-link></li>
          <li><router-link to="/receipt">Receipt</router-link></li>
          <li><router-link to="/recipes">Recipe</router-link></li>
          <li><router-link to="/account">Account</router-link></li>
        </ul>
      </div>

      <div class="nav-right">
        <button class="icon-btn">🔔</button>
        <router-link to="/account">
          <img src="@/assets/user-avatar.png" alt="User" class="avatar" />
        </router-link>
      </div>
    </nav>

    <router-view />
  </div>
</template>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 24px;
}

.logo {
  width: 36px;
  height: 36px;
}

.nav-links {
  display: flex;
  gap: 18px;
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-links li a {
  text-decoration: none;
  color: black;
  font-weight: 500;
}

.nav-links li a.router-link-exact-active {
  font-weight: 700;
  color: black !important;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.icon-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}
</style>
