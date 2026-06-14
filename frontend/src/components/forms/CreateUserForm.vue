<template>
  <div class="create-user-box">
    <h3>Создать нового сотрудника</h3>
    <form @submit.prevent="submitForm">
      <input v-model="form.username" placeholder="Логин" required />
      <input v-model="form.password" type="password" placeholder="Пароль" required />

      <label>Роль:</label>
      <select v-model="form.role">
        <!-- Показываем роль Админа только если текущий пользователь сам Админ -->
        <option v-if="currentUserRole === 'ADMINISTRATOR'" value="ADMINISTRATOR">Администратор</option>
        <option value="MANAGER">Менеджер</option>
        <option value="PERFORMER">Исполнитель</option>
      </select>

      <button type="submit">Создать</button>
    </form>
    <p v-if="message" class="info">{{ message }}</p>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import axios from 'axios';

const currentUserRole = ref(localStorage.getItem('role'));

const form = reactive({
  username: '',
  password: '',
  role: 'PERFORMER'
});

const message = ref('');

const submitForm = async () => {
  try {

    const token = localStorage.getItem('token');
    await axios.post('/api/users', form, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    message.value = 'Пользователь успешно создан!';
    form.username = '';
    form.password = '';
  } catch (error) {
    message.value = error.response?.data?.message || 'Ошибка при создании';
  }
};
</script>