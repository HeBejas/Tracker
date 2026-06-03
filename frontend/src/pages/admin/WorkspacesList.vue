<template>
  <AdminNavHeaderComponent :links="adminLinks" />
  <AdminWrapperComponent>

      <AdminHeaderComponent title="Список рабочих сред">
        <button class="action-btn primary">Создать рабочую среду</button>
      </AdminHeaderComponent>

      <DataBaseTable
          :columns="projectColumns"
          :data="projects"
          mode="Inspect"
          :isLoading="isLoading"
          @inspect="goToProject"
      />

  </AdminWrapperComponent>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import DataBaseTable from '../../components/tables/DataBaseTable.vue'
import type { TableColumn } from '../../components/tables/DataBaseTable.vue'

import AdminNavHeaderComponent from '../../components/admin/AdminNavHeaderComponent.vue'
import AdminWrapperComponent from '../../components/admin/AdminWrapperComponent.vue'
import AdminHeaderComponent from '../../components/admin/AdminHeaderComponent.vue'

const adminLinks = [
  '/admin/workspaces',
  '/admin/users',
  '/admin/tariffs'
]

const router = useRouter()
const isLoading = ref(true)
const projects = ref<any[]>([])

const projectColumns: TableColumn[] = [
  { key: 'id', label: 'ID' },
  { key: 'title', label: 'Название проекта' },
  { key: 'manager', label: 'Руководитель' },
  { key: 'status', label: 'Статус' }
]

const fetchProjects = async () => {
  isLoading.value = true

  // Имитация задержки сети при запросе через Axios
  setTimeout(() => {
    projects.value = [
      { id: 1, title: 'Система управления задачами (Tracker)', manager: 'Александров Б. К.', status: 'Активен' },
      { id: 2, title: 'База данных ИжГТУ', manager: 'Иванов А. А.', status: 'На паузе' },
      { id: 3, title: 'Разработка API для библиотеки', manager: 'Смирнов В. И.', status: 'В разработке' },
      { id: 4, title: 'Мобильное приложение книжного', manager: 'Петров Г. Г.', status: 'Завершен' },
      { id: 5, title: 'Интеграция Spring Boot Security', manager: 'Александров Б. К.', status: 'Активен' },
      { id: 6, title: 'Анализ распределений (Matplotlib)', manager: 'Сидоров П. П.', status: 'Завершен' }
    ]
    isLoading.value = false
  }, 800)
}

const goToProject = (projectId: number) => {
  router.push(`/admin/workspaces/${projectId}`)
}

onMounted(() => {
  fetchProjects()
})
</script>