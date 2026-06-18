<template>
  <FrameWrapperComponent>
    <FrameHeaderComponent>
      Мои задачи
    </FrameHeaderComponent>

    <TableComponent
        :columns="output_columns"
        :data="tasks"
        @row-click="handleTaskClick"
    >
      <template #action>
        </template>
    </TableComponent>
  </FrameWrapperComponent>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

import FrameWrapperComponent from '../../components/frame/FrameWrapperComponent.vue'
import FrameHeaderComponent from '../../components/frame/FrameHeaderComponent.vue'
import TableComponent, { type TableColumn } from '../../components/tables/TableComponent.vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const tasks = ref<any[]>([])

const output_columns: TableColumn[] = [
  { key: 'name', label: 'Название задачи', sortable: true, name_field: true },
  { key: 'projectName', label: 'Проект', sortable: true, filterable: true },
  { 
    key: 'roleId', 
    label: 'Роль', 
    sortable: true, 
    filterable: true, 
    options: [
      { value: 1, label: 'Исполнитель'},
      { value: 2, label: 'Тестировщик'},
      { value: 3, label: 'Заказчик'},
      { value: 4, label: 'Наблюдатель'},
    ]
  },
  { key: 'statusId', label: 'Статус', sortable: true, filterable: true,
    options: [
      { value: 1, label: 'Новая', colorClass: 'badge badge-new' },
      { value: 2, label: 'В работе', colorClass: 'badge badge-working' },
      { value: 3, label: 'На проверке', colorClass: 'badge badge-check' },
      { value: 4, label: 'Завершена', colorClass: 'badge badge-done' },
      { value: 5, label: 'Отменена', colorClass: 'badge badge-cancelled' }
    ]
  },
  { key: 'priorityId', label: 'Приоритет', sortable: true, filterable: true,
    options: [
      { value: 1, label: 'Низкий', colorClass: 'badge badge-white'  },
      { value: 2, label: 'Средний', colorClass: 'badge badge-blue' },
      { value: 3, label: 'Высокий', colorClass: 'badge badge-red' },
      { value: 4, label: 'Критичный', colorClass: 'badge badge-yellow' }
    ]
  },
  { key: 'deadlineDate', label: 'Дедлайн', type: 'date', sortable: true, filterable: true },
  { key: 'createdAt', label: 'Создана', type: 'date', sortable: true, filterable: true }
]

const handleTaskClick = (task: any) => {
  router.push({
    name: 'WorkspaceTaskPage',
    params: {
      id: route.params.id,
      taskId: task.id
    },
    state: { taskName: task.name, projectId: task.projectId }
  })
}

const fetchMyTasks = async () => {
  const workspaceId = route.params.id
  const userId = authStore.userId 

  if (!workspaceId || !userId) {
    console.warn('Не найден workspaceId или userId')
    return
  }

  try {
    const response = await axios.get('/api/tasks/my', {
      params: { 
        workspaceId: workspaceId,
        userId: userId 
      }
    })
    tasks.value = response.data
  } catch (error) {
    console.error(`Ошибка при загрузке моих задач: ${error}`)
  }
}

onMounted(() => {
  fetchMyTasks()
})
</script>