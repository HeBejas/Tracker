<template>
  <div class="change-password-section">
    <button class="change-password-btn" @click="showChangePasswordModal = true">
      🔒 Сменить пароль
    </button>
    <ChangePasswordModal
        :show="showChangePasswordModal"
        title="Смена пароля"
        @close="showChangePasswordModal= false"
        @submit="handleChangePassword"
    />
    <p v-if="message" :class="messageType">{{ message }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../../stores/auth'
import ChangePasswordModal from '../modals/ChangePasswordModal.vue'

const authStore = useAuthStore()

const showChangePasswordModal = ref(false)

const message = ref('')
const messageType = ref<'success' | 'error'>('success')

const handleChangePassword = async (data: { oldPassword: string; newPassword: string }) => {
  console.log(123)
  try {
    message.value = ''

    if (!data.oldPassword.trim() || !data.newPassword.trim()) {
      message.value = 'Поля должны быть заполнены'
      messageType.value = 'error'
      return
    }

    if (data.oldPassword === data.newPassword) {
      message.value = 'Новый пароль не должен совпадать со старым'
      messageType.value = 'error'
      return
    }

    await axios.put(`/api/auth/change-password/${authStore.userId}`, {
      oldPassword: data.oldPassword,
      newPassword: data.newPassword
    })

    message.value = 'Пароль успешно изменён'
    messageType.value = 'success'
    showChangePasswordModal.value = false

  } catch (error: any) {
    message.value = error.response?.data || 'Ошибка при смене пароля'
    messageType.value = 'error'
  }
}
</script>

<style scoped>
.change-password-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px 20px;
  /* Синий цвет в стиль аватара на странице профиля */
  background-color: #2563eb;
  color: #ffffff;
  border: 1px solid transparent;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(37, 99, 235, 0.2);
  transition: all 0.2s ease;
  width: 100%; /* Занимает всю доступную ширину во флексе */
}
.change-password-btn:hover {
  background-color: #1d4ed8;
  box-shadow: 0 4px 6px rgba(37, 99, 235, 0.3);
}

.change-password-btn:active {
  background-color: #1e40af;
  transform: translateY(0);
}
</style>