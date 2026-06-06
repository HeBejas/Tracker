<template>
  <aside class="sidebar" :class="{ 'collapsed': isCollapsed }">

    <div class="sidebar-top">
      <div class="logo-container">
<!--        <div class="logo-icon">💠</div>-->
        <div v-show="!isCollapsed" class="logo-text">Трекер</div>
      </div>

      <nav class="nav-group">
        <SideBarNavItem v-if="authStore.userRole !== 'admin'"  to="/tasks" :isCollapsed="isCollapsed">
          <template #icon>📋</template>
          Задачи
        </SideBarNavItem>

        <SideBarNavItem v-if="authStore.userRole !== 'admin'" to="/projects" :isCollapsed="isCollapsed">
          <template #icon>📁</template>
          Проекты
        </SideBarNavItem>

        <SideBarNavItem v-if="authStore.userRole !== 'admin'" to="/history" :isCollapsed="isCollapsed">
          <template #icon>⏱️</template>
          История
        </SideBarNavItem>

        <SideBarNavItem v-if="authStore.userRole !== 'admin'" to="/history" :isCollapsed="isCollapsed">
          <template #icon>👥</template>
          Добавить Пользователя
        </SideBarNavItem>

        <SideBarNavItem v-if="authStore.userRole === 'admin'" to="/admin" :isCollapsed="isCollapsed">
          <template #icon>🛡️</template>
          Администрирование
        </SideBarNavItem>
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
import { ref, onMounted } from 'vue'
import SideBarNavItem from '../nav/SideBarNavItem.vue'
import { useAuthStore } from '../../stores/auth'

const isCollapsed = ref(false)
const authStore = useAuthStore()
const toggleSettingsPanel = () => {
  console.log('Панель меню')

}

onMounted(() =>{
  console.log( authStore.userRole)
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

<!--<script setup lang="ts">-->
<!--</script>-->
<!--<template>-->
<!--  <aside>-->
<!--    <div class="logo">MyTracker</div>-->
<!--    <nav>-->
<!--      <router-link to="/tasks">Задачи</router-link>-->
<!--      <router-link to="/">Проекты</router-link>-->
<!--      <router-link to="/">История</router-link>-->
<!--      <router-link to="/">Добавить пользователя</router-link>-->
<!--      <router-link to="/">Администрирование</router-link>-->


<!--    </nav>-->
<!--    Тут снизу меню-->
<!--    <router-link to="/">Уведомления</router-link>-->
<!--    <router-link to="/">Настройки</router-link>-->
<!--    <router-link to="/">Учетная запись</router-link>-->
<!--    Тут кнопка которая ужимает и расширяет меню со стреклой которая меняется в зависимости от состояния меню-->
<!--  </aside>-->
<!--</template>-->

<!--<style scoped>-->
<!--nav {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  gap: 10px;-->
<!--  padding: 20px;-->
<!--}-->

<!--nav a {-->
<!--  color: white;-->
<!--  text-decoration: none;-->
<!--}-->

<!--/* Vue автоматически вешает этот класс на активную ссылку */-->
<!--nav a.router-link-active {-->
<!--  font-weight: bold;-->
<!--  color: #42b983;-->
<!--}-->
<!--</style>-->