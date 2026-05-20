<template>
  <div class="dashboard-container">
    <h1>Панель управления</h1>
    <p>Вы вошли как: <strong>{{ userRole }}</strong></p>

    <hr />

    <!-- Форму видят ТОЛЬКО Администраторы и Менеджеры -->
    <div v-if="userRole === 'ADMINISTRATOR' || userRole === 'MANAGER'" class="create-user-section">
      <h3>Создать нового сотрудника</h3>

      <form @submit.prevent="handleCreateUser">
        <div class="form-group">
          <label>Логин:</label>
          <input v-model="form.username" type="text" required />
        </div>

        <div class="form-group">
          <label>Пароль:</label>
          <input v-model="form.password" type="password" required />
        </div>

        <div class="form-group">
          <label>Роль сотрудника:</label>
          <select v-model="form.role">
            <!-- Админ видит всех, Менеджер — только Менеджеров и Исполнителей -->
            <option v-if="userRole === 'ADMINISTRATOR'" value="ADMINISTRATOR">Администратор</option>
            <option value="MANAGER">Менеджер</option>
            <option value="PERFORMER">Иполнитель</option>
          </select>
        </div>

        <button type="submit">Создать</button>
      </form>

      <p v-if="statusMessage" :class="{ 'success-text': isSuccess, 'error-text': !isSuccess }">
        {{ statusMessage }}
      </p>
    </div>

    <!-- Интерфейс для обычного Исполнителя -->
    <div v-else class="performer-section">
      <p>У вас роль Исполнителя. Доступные вам задачи будут отображаться здесь.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';

const userRole = ref('');
const statusMessage = ref('');
const isSuccess = ref(false);

const form = reactive({
  username: '',
  password: '',
  role: 'PERFORMER'
});

onMounted(() => {
  // Достаем роль, которую бэкенд сохранил в localStorage при логине
  userRole.value = localStorage.getItem('role') || 'PERFORMER';
});

const handleCreateUser = async () => {
  try {
    statusMessage.value = '';
    const token = localStorage.getItem('token');

    // Стучимся на бэкенд-ручку Spring Boot
    await axios.post('/api/users', form, {
      headers: { 'Authorization': `Bearer ${token}` }
    });

    statusMessage.value = 'Пользователь успешно создан!';
    isSuccess.value = true;

    // Сбрасываем поля
    form.username = '';
    form.password = '';
  } catch (error: any) {
    isSuccess.value = false;
    statusMessage.value = error.response?.data || 'Не удалось создать пользователя';
  }
};
</script>

<style scoped>
.dashboard-container { padding: 30px; font-family: sans-serif; }
.create-user-section { max-width: 400px; margin-top: 20px; border: 1px solid #ccc; padding: 20px; border-radius: 8px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; }
input, select { width: 100%; padding: 8px; box-sizing: border-box; }
button { padding: 10px 15px; background-color: #4caf50; color: white; border: none; cursor: pointer; border-radius: 4px; }
button:hover { background-color: #45a049; }
.success-text { color: green; margin-top: 10px; }
.error-text { color: red; margin-top: 10px; }
</style>