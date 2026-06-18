<template>
  <div class="select-user-wrapper">
    <div class="select-input-container">
      <input
        type="text"
        v-model="searchQuery"
        @focus="showDropdown = true"
        @blur="handleBlur"
        placeholder="Поиск сотрудника..."
        class="pro-input select-user-input"
      />
      <svg
        class="chevron-icon"
        xmlns="http://www.w3.org/2000/svg"
        viewBox="0 0 20 20"
        fill="currentColor"
      >
        <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
      </svg>

      <div v-if="showDropdown" class="dropdown-menu">
        <div v-if="filteredUsers.length === 0" class="no-users">
          Сотрудники не найдены
        </div>
        <div
          v-for="user in filteredUsers"
          :key="user.id"
          class="dropdown-item"
          @click="selectUser(user)"
        >
          {{ user.fullName }}
        </div>
      </div>
    </div>

    <div v-if="selectedParticipants.length > 0" class="participants-list">
      <div
        v-for="participant in selectedParticipants"
        :key="`${participant.userId}-${participant.roleId}`"
        class="participant-item badge"
      >
        <span class="participant-name">{{ participant.userName }}</span>
        <button
          @click="removeParticipant(participant)"
          class="remove-btn"
          type="button"
        >
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import type { TaskParticipant } from '@/types/task'

interface User {
  id: number
  fullName: string
}

const props = defineProps<{
  workspaceId: number
  participants: TaskParticipant[]
}>()

const emit = defineEmits<{
  (e: 'add:participant', payload: { userId: number, roleId: number }): void
  (e: 'remove:participant', payload: TaskParticipant): void
}>()

const searchQuery = ref('')
const showDropdown = ref(false)
const users = ref<User[]>([])
const selectedParticipants = ref<TaskParticipant[]>([])

const filteredUsers = computed(() => {
  const query = searchQuery.value.toLowerCase()
  return users.value.filter(user => {
    const isAlreadySelected = selectedParticipants.value.some(p => p.userId === user.id)
    return !isAlreadySelected && user.fullName.toLowerCase().includes(query)
  })
})

const handleBlur = () => {
  setTimeout(() => {
    showDropdown.value = false
    searchQuery.value = ''
  }, 200)
}

const selectUser = (user: User) => {
  const roleId = 1
  const newParticipant: TaskParticipant = {
    userId: user.id,
    userName: user.fullName,
    roleId,
    assignedAt: new Date().toISOString()
  }
  selectedParticipants.value.push(newParticipant)
  emit('add:participant', { userId: user.id, roleId })
  searchQuery.value = ''
  showDropdown.value = false
}

const removeParticipant = (participant: TaskParticipant) => {
  selectedParticipants.value = selectedParticipants.value.filter(
    p => p.userId !== participant.userId || p.roleId !== participant.roleId
  )
  emit('remove:participant', participant)
}

const fetchWorkspaceUsers = async () => {
  try {
    const response = await axios.get(`/api/workspaces/${props.workspaceId}/users`)
    users.value = response.data
  } catch (error) {
    console.error('Ошибка при загрузке сотрудников:', error)
  }
}

onMounted(() => {
  selectedParticipants.value = [...props.participants]
  fetchWorkspaceUsers()
})
</script>

<style scoped>
.select-user-wrapper {
  display: flex;
  flex-direction: column;
  gap: 0;
  width: 100%;
}

.select-input-container {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

.select-user-input {
  width: 100%;
  padding: 4px 8px;
  padding-right: 28px;
  font-size: 14px;
  color: #111827;
  background-color: #ffffff;
  border: 1px solid #d1d5db; 
  border-radius: 6px;
  outline: none;
  font-family: inherit;
  box-sizing: border-box;
  transition: all 0.2s ease;
}

.select-user-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.chevron-icon {
  position: absolute;
  right: 8px;
  width: 16px;
  height: 16px;
  color: #9ca3af;
  pointer-events: none;
  transition: transform 0.2s;
}

/* Выпадашка теперь перекрывает всё внизу */
.dropdown-menu {
  position: absolute;
  top: calc(100% + 4px); /* Чуть-чуть отступаем от инпута */
  left: 0;
  right: 0;
  z-index: 50; /* Жестко поверх всех списков и панелей */
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  max-height: 180px;
  overflow-y: auto;
}

.no-users {
  padding: 12px;
  text-align: center;
  color: #9ca3af;
  font-size: 13px;
}

.dropdown-item {
  padding: 8px 12px;
  cursor: pointer;
  color: #111827;
  font-size: 13px;
  transition: background-color 0.15s;
}

.dropdown-item:hover {
  background-color: #f3f4f6;
}

/* Список выбранных участников теперь Flex Wrap (переносится на новые строки) */
.participants-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 8px;
}

/* Сделал их в виде аккуратных бэйджей, чтобы не смотрелись огромными строками */
.participant-item {
  display: inline-flex;
  align-items: center;
  padding: 2px 8px;
  background-color: #f3f4f6;
  border-radius: 12px;
  font-size: 13px;
  color: #374151;
  border: 1px solid #e5e7eb;
}

.participant-name {
  max-width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.remove-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  padding: 0;
  margin-left: 4px;
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  transition: color 0.15s;
}

.remove-btn:hover {
  color: #ef4444; /* Краснеет только при наведении, чтобы не мозолить глаза */
}

.remove-btn svg {
  width: 12px;
  height: 12px;
}
</style>