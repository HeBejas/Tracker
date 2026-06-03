<template>
  <AdminNavHeaderComponent :links="projectLinks" />
  <AdminWrapper>
    <div class="layout">
      <header class="header">
        <div class="header-left">
          <button class="back-btn" @click="goBack" title="Вернуться к списку">
            <span class="arrow">←</span> Назад
          </button>

          <div class="divider"></div>

          <div class="title-wrapper">
            <span class="subtitle">Управление рабочей средой</span>
            <h2>{{ projectId }}</h2>
          </div>
        </div>

<!--        <div class="header-right">-->
<!--          <button class="action-btn primary">+ Новая задача</button>-->
<!--        </div>-->
      </header>

      <div class="project-content">
        <router-view />
      </div>

    </div>
  </AdminWrapper>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
// Убедись, что путь до компонента правильный для твоей структуры папок
import AdminNavHeaderComponent from '../../../components/admin/AdminNavHeaderComponent.vue'
import AdminWrapper from "../../../components/admin/AdminWrapperComponent.vue";

const route = useRoute()
const router = useRouter()

// Получаем ID из URL (например, из /admin/projects/123 достанет "123")
const projectId = route.params.id

// Формируем ссылки для хедера. Пути должны строго совпадать с твоим router.js!
const projectLinks = computed(() => {
  return [
    `/admin/workspaces/${projectId}/dashboard`,
    `/admin/workspaces/${projectId}/tasks`,
    `/admin/workspaces/${projectId}/settings`
  ]
})

const goBack = () => {
  router.push('/admin/workspaces')
}
</script>
<style scoped>
.layout {
  display: flex;
  flex-direction: column;
}

/* Шапка проекта с красивым подчеркиванием */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #e2e8f0;
  margin-bottom: 24px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* Облегченная кнопка "Назад" (выглядит как аккуратная ссылка) */
.back-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  background: transparent;
  border: none;
  color: #64748b;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  padding: 6px 12px 6px 4px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.back-btn .arrow {
  font-size: 1.1rem;
  transition: transform 0.2s ease;
}

.back-btn:hover {
  background: #f1f5f9;
  color: #0f172a;
}

.back-btn:hover .arrow {
  transform: translateX(-3px); /* Легкая анимация стрелочки при наведении */
}

/* Вертикальная черта между кнопкой "Назад" и заголовком */
.divider {
  width: 1px;
  height: 32px;
  background-color: #e2e8f0;
}

/* Двухэтажный заголовок */
.title-wrapper {
  display: flex;
  flex-direction: column;
}

.title-wrapper .subtitle {
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  color: #94a3b8;
  letter-spacing: 0.05em;
  margin-bottom: 2px;
}

.title-wrapper h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #0f172a;
  font-weight: 600;
}

/* Контент занимает оставшееся место, двойные рамки удалены */
.project-content {
  flex-grow: 1;
}

/* Кнопки */
.action-btn {
  padding: 8px 16px;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  border: none;
  font-size: 0.9rem;
  transition: all 0.2s ease;
}

.action-btn.primary {
  background: #2563eb;
  color: white;
  box-shadow: 0 1px 2px rgba(37, 99, 235, 0.15);
}

.action-btn.primary:hover {
  background: #1d4ed8;
  box-shadow: 0 2px 4px rgba(37, 99, 235, 0.25);
}
</style>