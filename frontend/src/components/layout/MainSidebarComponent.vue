<template>
  <aside class="sidebar" :class="{ 'collapsed': isCollapsed }">

    <div class="sidebar-top">
      <div class="logo-container">
<!--        <div class="logo-icon">💠</div>-->
        <div v-show="!isCollapsed" class="logo-text">Трекер</div>
      </div>

      <nav class="nav-group">
        <SideBarNavItem v-for="item in menuItems" :key="item.to" :to="item.to" :isCollapsed="isCollapsed">
          <template #icon>{{ item.icon }}</template>
          {{ item.label }}
        </SideBarNavItem>
<!--        <SideBarNavItem v-if="authStore.userRole !== 'admin'"  to="/tasks" :isCollapsed="isCollapsed">-->
<!--          <template #icon>📋</template>-->
<!--          Задачи-->
<!--        </SideBarNavItem>-->

<!--        <SideBarNavItem v-if="authStore.userRole !== 'admin'" to="/projects" :isCollapsed="isCollapsed">-->
<!--          <template #icon>📁</template>-->
<!--          Проекты-->
<!--        </SideBarNavItem>-->

<!--        <SideBarNavItem v-if="authStore.userRole !== 'admin'" to="/history" :isCollapsed="isCollapsed">-->
<!--          <template #icon>⏱️</template>-->
<!--          История-->
<!--        </SideBarNavItem>-->

<!--        <SideBarNavItem v-if="authStore.userRole !== 'admin'" to="/history" :isCollapsed="isCollapsed">-->
<!--          <template #icon>👥</template>-->
<!--          Сотрудники-->
<!--        </SideBarNavItem>-->

<!--        <SideBarNavItem v-if="authStore.userRole === 'admin'" to="/admin" :isCollapsed="isCollapsed">-->
<!--          <template #icon>🛡️</template>-->
<!--          Администрирование-->
<!--        </SideBarNavItem>-->
      </nav>

      <div class="action-container">
        <button class="create-btn" v-if="authStore.userRole !== 'admin'"  :class="{ 'btn-collapsed': isCollapsed }">
          <span v-if="!isCollapsed">+ Создать задачу</span>
          <span v-else>+</span>
        </button>
      </div>
    </div>

    <div class="sidebar-bottom">
      <nav class="nav-group">
        <SideBarNavItem to="/notifications" :isCollapsed="isCollapsed">
          <template #icon>🔔</template>
          Уведомления
        </SideBarNavItem>

        <SideBarNavItem
            :isCollapsed="isCollapsed"
            @click="toggleSettingsPanel"
            class="settings-item"
        >
          <template #icon>⚙️</template>
          Настройки
        </SideBarNavItem>

        <SideBarNavItem to="/profile" :isCollapsed="isCollapsed">
          <template #icon>👤</template>
          Учетная запись
        </SideBarNavItem>
      </nav>

      <div class="collapse-trigger" @click="isCollapsed = !isCollapsed">
        <svg
            class="arrow-icon"
            :class="{ 'rotated': isCollapsed }"
            width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
        >
          <polyline points="15 18 9 12 15 6"></polyline>
        </svg>
      </div>
    </div>

  </aside>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import SideBarNavItem from '../nav/SideBarNavItem.vue'
import { useAuthStore } from '../../stores/auth'

const route = useRoute()
const authStore = useAuthStore()

const workspaceId = computed(() => {
  if (route.params.id) return route.params.id;
  return authStore.workspaceId;
});

const getPath = (path) => {
  if (authStore.userRole === 'admin') return path
  return `/workspaces/${workspaceId.value}${path}`
}



const menuItems = computed(() => {
  if (authStore.isAdmin) {
    return [
      { to: '/admin/', label: 'Администрирование', icon: '🛡️' },
      { to: '/admin/workspaces', label: 'Воркспейсы', icon: '📁️' },
      { to: '/admin/users', label: 'Пользователи', icon: '👥' },
      { to: '/admin/tariffs', label: 'Тарифы', icon: '💰' }
    ]
  }
  return [
    { to: `/workspaces/${workspaceId.value}/dashboard`, label: 'Дашборд', icon: '📋' },
    { to: `/workspaces/${workspaceId.value}/projects`, label: 'Проекты', icon: '📁' },
    { to: `/workspaces/${workspaceId.value}/employees`, label: 'Сотрудники', icon: '👥' },
    { to: `/workspaces/${workspaceId.value}/reports`, label: 'Отчеты', icon: '📊' }
  ]
})

const isCollapsed = ref(false)

const toggleSettingsPanel = () => {
  console.log('Панель меню')
}

onMounted(() =>{
  // console.log( authStore.userRole)
})
</script>

<style scoped>

.sidebar {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 240px;
  height: 100vh;
  border-right: 1px solid #e0e0e0;
  transition: width 0.3s ease;
  box-sizing: border-box;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-top {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 16px;
  height: 60px;
  box-sizing: border-box;
}

.logo-icon {
  font-size: 24px;
  flex-shrink: 0;
}

.logo-text {
  font-weight: bold;
  font-size: 18px;
  color: #333;
  white-space: nowrap;
}

.nav-group {
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding: 8px 0;
}

.action-container {
  padding: 16px 12px;
}

.create-btn {
  width: 100%;
  background-color: #4f46e5;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 10px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s, padding 0.2s;
  white-space: nowrap;
}

.create-btn:hover {
  background-color: #4338ca;
}

.create-btn.btn-collapsed {
  padding: 10px 0;
  font-size: 18px;
}

.sidebar-bottom {
  border-top: 1px solid #e0e0e0;
  padding-bottom: 8px;
}

.collapse-trigger {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 40px;
  cursor: pointer;
  background-color: #f8f9fa;
  transition: background-color 0.2s;
}

.collapse-trigger:hover {
  background-color: #e9ecef;
}

.arrow-icon {
  transition: transform 0.3s ease;
}

.arrow-icon.rotated {
  transform: rotate(180deg);
}

/* Имитация активного стейта для не-роут элементов (например, когда открыты настройки) */
.settings-item:active {
  background-color: #eef2ff;
}
</style>
