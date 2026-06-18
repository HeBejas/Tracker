<template>
  <div v-if="!$route.meta.HideLayout && $route.path !== '/'" class="app-layout">
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

  <div class="toast-container">
    <ToastComponent
        v-for="toast in toasts"
        :key="toast.id"
        :id="toast.id"
        :message="toast.message"
        :type="toast.type"
        :duration="toast.duration"
        @remove="removeToast"
    />
  </div>



</template>

<script setup>
import './assets/main.css'
import AppSidebar from './components/layout/MainSidebarComponent.vue'
import AppHeader from './components/layout/MainHeaderComponent.vue'
import ToastComponent from './components/toasts/ToastComponent.vue'
import { useToast } from './utils/ToastUtils.ts'

const { toasts, removeToast } = useToast()
</script>

<style scoped>

.blank-layout {
  width: 100vw;
  height: 100vh;
}

.app-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
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
}
</style>
