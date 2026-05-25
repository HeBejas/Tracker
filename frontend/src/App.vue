<template>
  <div v-if="$route.meta.requiresLayout !== false" class="app-layout">
    <AppSidebar class="sidebar" />

    <div class="main-wrapper">
      <AppHeader class="header" />
      <main class="content-area">
        <router-view />
      </main>
    </div>
  </div>

  <div v-else class="blank-layout">
    <router-view />
  </div>
</template>

<script setup>
import './assets/main.css'
import AppSidebar from './components/layout/MainSidebarComponent.vue'
import AppHeader from './components/layout/MainHeaderComponent.vue'
</script>

<style>
body {
  margin: 0;
  font-family: sans-serif;
}

/* Стили для формы логина (на весь экран, без меню) */
.blank-layout {
  width: 100vw;
  height: 100vh;
}

/* Наш стандартный каркас для внутренних страниц */
.app-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  width: 250px;
  background-color: #2c3e50;
  color: white;
}

.main-wrapper {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  background-color: #ecf0f1;
  border-bottom: 1px solid #ccc;
}

.content-area {
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f9f9f9;
}
</style>


<!--<script setup>-->
<!--import LoginPage from './pages/LoginPage.vue'-->
<!--import './assets/main.css'-->
<!--</script>-->

<!--<template>-->
<!--  <LoginPage />-->
<!--</template>-->
