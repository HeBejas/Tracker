<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router' // 1. Импортируй useRouter

const router = useRouter() // 2. Инициализируй роутер

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
    const response = await axios.post('http://localhost:8080/api/auth/login', {
      email: email.value,
      password: password.value
    })
    alert(`Вход выполнен!: ${response.data}`)
  } catch (error) {
    alert("Неверный email или пароль")
  }
}
</script>

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
</style>