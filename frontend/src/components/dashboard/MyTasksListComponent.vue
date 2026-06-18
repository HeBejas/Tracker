<template>
  <div class="dashboard-wrapper">
    <div class="dashboard-header">
      <h2 class="dashboard-title">Мои задачи</h2>
      
      <div class="header-actions">
        <div class="filter-tabs">
          <button
              v-for="filter in filters"
              :key="filter.id"
              class="filter-tab"
              :class="{ active: activeFilter === filter.id }"
              @click="activeFilter = filter.id"
          >
            {{ filter.label }}
          </button>
        </div>
      </div>
    </div>

    <div class="dashboard-card">
      <div class="list-container" v-if="filteredTasks.length > 0">
        <div
            v-for="task in filteredTasks"
            :key="task.id"
            class="list-row"
            @click="selectTask(task)"
        >
          <div class="row-left">
            <div class="row-info">
              <h4 class="row-title">{{ task.name }}</h4>
              <p class="row-subtitle" v-if="task.projectName">{{ task.projectName }}</p>
            </div>
          </div>
          
          <div class="row-right">
            <span class="badge" :class="getStatusInfo(task.statusId).colorClass">
              {{ getStatusInfo(task.statusId).label }}
            </span>
            
            <span class="badge" :class="getPriorityInfo(task.priorityId).colorClass">
              {{ getPriorityInfo(task.priorityId).label }}
            </span>
            
            <span class="row-date" v-if="task.deadlineDate">
              {{ formatDate(task.deadlineDate) }}
            </span>
          </div>
        </div>
      </div>

      <div class="empty-state" v-else>
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <path d="M9 12h6m-6 4h6m2-9H7a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2V9a2 2 0 00-2-2z" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
        </svg>
        <p>Задач не найдено</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import type { Task } from '@/types/task'
import { formatUIDate } from '@/utils/dateUtils'

const props = defineProps<{
  tasks: Task[]
}>()

const emit = defineEmits<{
  (e: 'select', task: Task): void
}>()

const filters = [
  { id: 'all', label: 'Все' },
  { id: 1, label: 'Новые' },
  { id: 2, label: 'В работе' },
  { id: 3, label: 'На проверке' },
  { id: 4, label: 'Завершено' },
  { id: 5, label: 'Отменено' }
]

const activeFilter = ref<string | number>('all')

const filteredTasks = computed(() => {
  if (!props.tasks) return [] 
  
  if (activeFilter.value === 'all') {
    return props.tasks
  }
  
  return props.tasks.filter(t => t.statusId === activeFilter.value)
})

const selectTask = (task: Task) => {
  emit('select', task)
}

const getStatusInfo = (statusId: number) => {
  const statuses: Record<number, { label: string, colorClass: string }> = {
    1: { label: 'Новые', colorClass: 'badge-new' },
    2: { label: 'В работе', colorClass: 'badge-working' },
    3: { label: 'На проверке', colorClass: 'badge-check' },
    4: { label: 'Завершены', colorClass: 'badge-done' },
    5: { label: 'Отменены', colorClass: 'badge-cancelled' }
  }
  return statuses[statusId] || { label: 'Неизвестно', colorClass: 'badge-white' }
}

const getPriorityInfo = (priorityId: number) => {
  const priorities: Record<number, { label: string, colorClass: string }> = {
    1: { label: 'Низкий', colorClass: 'badge-white' },
    2: { label: 'Средний', colorClass: 'badge-blue' },
    3: { label: 'Высокий', colorClass: 'badge-red' },
    4: { label: 'Критичный', colorClass: 'badge-yellow' }
  }
  return priorities[priorityId] || { label: 'Неизвестно', colorClass: 'badge-white' }
}

const formatDate = (date: string | null): string => {
  if (!date) return ''
  return formatUIDate(date) || ''
}
</script>