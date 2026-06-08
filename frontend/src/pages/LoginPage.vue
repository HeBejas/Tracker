<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>Вход в систему</h2>
      <div class="form-group">
        <label>Электронная почта</label>
        <input v-model="email" type="email" placeholder="example@mail.com" />
      </div>

      <div class="form-group">
        <label>Пароль</label>
        <input v-model="password" type="password" placeholder="••••••••" />
      </div>

      <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>

      <button class="submit-btn" @click="handleLogin">Войти</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router' // 1. Импортируй useRouter
import { useAuthStore } from '../stores/auth'

const router = useRouter() // 2. Инициализируй роутер
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const errorMessage = ref('')

// Переменные для проверки связи с БД
const dbConnectionError = ref('')

// Функция, которая идет на бэкенд за ролями

// Запускаем проверку автоматически, как только страница откроется в браузере
// onMounted(() => {
//   fetchRolesFromDb()
// })

const handleLogin = async () => {
  errorMessage.value = ''
  try {
    const response = await axios.post('/api/auth/login', {
      email: email.value,
      password: password.value
    })
    const data = response.data
    const token = 'fake-token-for-dev'
    authStore.login(token, data.userId, data.role, data.fullName, data.email)
    router.push('/home')
  } catch (error) {
    alert("Неверный email или пароль")
  }
}
</script>

<style scoped>
/* Доп стили для плашки проверки, чтобы визуально отличалась */
.db-test-zone {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 6px;
  border: 1px dashed #ccc;
  margin-bottom: 15px;
  font-size: 13px;
}
.db-test-zone h4 {
  margin: 0 0 8px 0;
  color: #555;
}
.success-text {
  color: #2ecc71;
  font-weight: bold;
  margin: 0 0 5px 0;
}
.roles-list {
  margin: 0;
  padding-left: 20px;
  color: #333;
}
hr {
  border: 0;
  border-top: 1px solid #eee;
  margin: 20px 0;
}






.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 20px;
}


.form-group:last-of-type {
  margin-bottom: 24px;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #334155;
  letter-spacing: 0.02em;
}

.form-group input {
  padding: 10px 14px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 14px;
  color: #0f172a;
  background-color: #ffffff;
  outline: none;
  transition: all 0.2s ease;
  font-family: inherit;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02) inset;
}

.form-group input:hover {
  border-color: #94a3b8;
}

.form-group input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.form-group input::placeholder {
  color: #94a3b8;
  opacity: 1;
}
</style>