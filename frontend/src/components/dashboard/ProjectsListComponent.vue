<template>
  <div class="dashboard-wrapper">
    <div class="dashboard-header">
      <h2 class="dashboard-title">Проекты</h2>
      
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

        <button class="btn-create" @click="createProject" v-if="canCreateProject" title="Создать проект">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 5a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H6a1 1 0 110-2h5V6a1 1 0 011-1z" />
          </svg>
        </button>
      </div>
    </div>

    <div class="dashboard-card">
      <div class="list-container" v-if="filteredProjects.length > 0">
        <div
            v-for="project in filteredProjects"
            :key="project.id"
            class="list-row"
            @click="selectProject(project)"
        >
          <div class="row-left">
            <div class="row-info">
              <h4 class="row-title">{{ project.name }}</h4>
              <p class="row-subtitle" v-if="project.description">
                {{ truncate(project.description, 80) }}
              </p>
            </div>
          </div>
          
          <div class="row-right">
            <span class="badge" :class="getStatusInfo(project.statusId).colorClass">
              {{ getStatusInfo(project.statusId).label }}
            </span>
            
            <span class="row-date">
              {{ formatDate(project.createdAt) }}
            </span>
          </div>
        </div>
      </div>

      <div class="empty-state" v-else>
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <path d="M9 12h6m-6 4h6m2-9H7a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2V9a2 2 0 00-2-2z" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
        </svg>
        <p>Нет проектов</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import type { Project } from '@/types/project'
import { formatUIDate } from '@/utils/dateUtils'

interface ProjectData extends Project {
  statusId: number
}

const props = defineProps<{
  projects: ProjectData[]
  canCreateProject?: boolean
}>()

const emit = defineEmits<{
  (e: 'create'): void
  (e: 'select', project: ProjectData): void
}>()

// Фильтры проектов
const filters = [
  { id: 'all', label: 'Все' },
  { id: 5, label: 'Новые' },
  { id: 1, label: 'В работе' },
  { id: 2, label: 'Приостановлены' },
  { id: 3, label: 'Завершены' },
  { id: 4, label: 'Отменены' }
]

const activeFilter = ref<string | number>('all')

const filteredProjects = computed(() => {
  if (!props.projects) return []
  
  if (activeFilter.value === 'all') {
    return props.projects
  }
  
  return props.projects.filter(p => p.statusId === activeFilter.value)
})

const createProject = () => {
  emit('create')
}

const selectProject = (project: ProjectData) => {
  emit('select', project)
}

// Новые статусы проектов
const getStatusInfo = (statusId: number) => {
  const statuses: Record<number, { label: string, colorClass: string }> = {
    1: { label: 'В работе', colorClass: 'badge-working' },
    2: { label: 'Приостановлен', colorClass: 'badge-paused' },
    3: { label: 'Завершен', colorClass: 'badge-completed' },
    4: { label: 'Отменен', colorClass: 'badge-cancelled' },
    5: { label: 'Новый', colorClass: 'badge-new' }
  }
  return statuses[statusId] || { label: 'Неизвестно', colorClass: 'badge-white' }
}

const formatDate = (date: string | null): string => {
  if (!date) return 'Дата неизвестна'
  return formatUIDate(date) || 'Дата неизвестна'
}

const truncate = (text: string, length: number): string => {
  return text.length > length ? text.substring(0, length) + '...' : text
}
</script>