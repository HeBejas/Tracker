<template>
  <div class="dashboard-nav-wrapper">
    <h2 class="section-title">Меню</h2>
    <div class="dashboard-tabs">
      <router-link
        v-for="tab in tabs"
        :key="tab.to"
        :to="tab.to"
        class="tab-item"
        active-class="active"
      >
        <div class="tab-icon">{{ tab.icon }}</div>
        <div class="tab-name">{{ tab.label }}</div>
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const workspaceId = computed(() => route.params.id || 0)

const tabs = computed(() => [
  { to: `/workspaces/${workspaceId.value}/my-tasks`, label: 'Мои Задачи', icon: '✅' },
  { to: `/workspaces/${workspaceId.value}/projects`, label: 'Проекты', icon: '📁' },
  { to: `/workspaces/${workspaceId.value}/employees`, label: 'Сотрудники', icon: '👥' },
  { to: `/workspaces/${workspaceId.value}/tasks`, label: 'Задачи', icon: '📌' },
  { to: `/workspaces/${workspaceId.value}/reports`, label: 'Отчеты', icon: '📊' }
])
</script>

<style scoped>
.dashboard-nav-wrapper{
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
  margin: 0;
}

.dashboard-tabs {
  display: flex;
  gap: 1rem;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-width: 80px;
  padding: 12px 8px;
  cursor: pointer;
  border-radius: 8px;
  border: 2px solid transparent;
  transition: all 0.2s ease;
  text-decoration: none;
}

.tab-item:hover {
  background-color: #f3f4f6;
  border-color: #d1d5db;
}

.tab-item.active {
  background-color: #dbeafe;
  border-color: #3b82f6;
}

.tab-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  background: white;
  border: 1px solid #e5e7eb;
  font-size: 24px; /* Размер эмодзи */
}

.tab-name {
  font-size: 12px;
  font-weight: 500;
  color: #374151;
  text-align: center;
}

.tab-item.active .tab-name {
  color: #3b82f6;
  font-weight: 600;
}
</style>