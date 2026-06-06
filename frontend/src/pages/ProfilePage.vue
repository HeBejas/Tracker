<template>
  <div class="account-page-wrapper">
    <div class="page-header">
      <h1 class="page-title">Учетная запись</h1>
      <p class="page-subtitle">Здесь отображается информация о вашем профиле</p>
    </div>

    <div class="account-card">
      <div class="account-avatar">
        <div class="avatar-circle">
          {{ authStore.userRole.charAt(0).toUpperCase() || 'U' }}
        </div>
      </div>

      <div class="account-details">
        <div class="info-group">
          <span class="label">ФИО</span>
          <span class="value">{{ userFullName }}</span>
        </div>

        <div class="info-group">
          <span class="label">Email</span>
          <span class="value">{{ userEmail }}</span>
        </div>

        <div class="info-group">
          <span class="label">Уровень доступа</span>
          <span class="badge">{{ userRoleName }}</span>
        </div>
      </div>

      <div class="account-actions">
        <button class="logout-btn" @click="handleLogout">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
          Выйти из аккаунта
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

authStore.userRole
// const userFullName = computed(() => authStore.user?.fullName || 'Не указано')
// const userEmail = computed(() => authStore.user?.email || 'Не указано')

// Маппинг роли из числа в красивый текст
const userRoleName = computed(() => {
  switch (authStore.userRole ) {
    case 'admin': return 'Администратор'
    case 'manager': return 'Менеджер'
    case 'employee': return 'Пользователь'
    default: return 'Неизвестно'
  }
})

// Функция выхода
const handleLogout = () => {
  // Вызываем функцию очистки из твоего стора.
  // Убедись, что в useAuthStore() есть action `logout()` который чистит токены.
  if (typeof authStore.logout === 'function') {
    authStore.logout()
  } else {
    // Временный фоллбек, если метода logout() еще нет в сторе
    authStore.$reset()
    localStorage.removeItem('token')
  }
  router.push('/login')
}
</script>

<style scoped>
.account-page-wrapper {
  max-width: 600px;
  margin: 40px auto;
  padding: 0 20px;
}

.page-header {
  margin-bottom: 24px;
  text-align: center;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: #0f172a;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.account-card {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 32px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.account-avatar {
  margin-bottom: 24px;
}

.avatar-circle {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
}

.account-details {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
  background: #f8fafc;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #f1f5f9;
}

.info-group {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e2e8f0;
  padding-bottom: 12px;
}

.info-group:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.label {
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.value {
  font-size: 15px;
  font-weight: 500;
  color: #0f172a;
}

.badge {
  background: #e0e7ff;
  color: #4f46e5;
  padding: 4px 10px;
  border-radius: 9999px;
  font-size: 13px;
  font-weight: 600;
}

.account-actions {
  width: 100%;
  display: flex;
  justify-content: center;
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background-color: #fef2f2;
  color: #ef4444;
  border: 1px solid #fecaca;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.logout-btn:hover {
  background-color: #fee2e2;
  border-color: #fca5a5;
}

.logout-btn:active {
  background-color: #fca5a5;
}
</style>