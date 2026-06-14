<template>
  <FrameWrapperComponent>
    <FrameHeaderComponent>
      Все задачи
    </FrameHeaderComponent>

    <TableComponent
        :columns="output_columns"
        :data="tasks"
        @row-click="handleTaskClick"
    >
      <template #action>
<!--        <CreateButton @click="showSubmitModal = true">-->
<!--          Создать задачу-->
<!--        </CreateButton>-->
      </template>
    </TableComponent>
  </FrameWrapperComponent>

  <SubmitModal
      :show="showSubmitModal"
      title="Создать новую задачу"
      text="Вы уверены, что хотите создать новую задачу?"
      submitLabel="Создать"
      @close="showSubmitModal = false"
      @submit="handleCreateNewTask"
  />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'
import FrameWrapperComponent from '../../components/frame/FrameWrapperComponent.vue'
import FrameHeaderComponent from '../../components/frame/FrameHeaderComponent.vue'
import TableComponent, { type TableColumn } from '../../components/tables/TableComponent.vue'
import CreateButton from '../../components/buttons/CreateBtn.vue'
import SubmitModal from '../../components/modals/SubmitModal.vue'

// Опционально: создайте интерфейс Task в types/task.ts
// import type { Task } from '@/types/task'

const route = useRoute()
const router = useRouter()
const tasks = ref<any[]>([]) // Замените any на Task, если есть интерфейс
const authStore = useAuthStore()

// Настраиваем колонки под задачи
const output_columns: TableColumn[] = [
  { key: 'name', label: 'Название задачи', sortable: true, name_field: true },
  { key: 'projectName', label: 'Проект', sortable: true, filterable: true }, // Новая колонка от бэкенда
  { key: 'statusId', label: 'Статус', sortable: true, filterable: true,
    options: [
      { value: 1, label: 'Новая' },
      { value: 2, label: 'В работе' },
      { value: 3, label: 'На проверке' },
      { value: 4, label: 'Завершена' },
      { value: 5, label: 'Отменена' },
    ]
  },
  { key: 'priorityId', label: 'Приоритет', sortable: true, filterable: true,
    options: [
      { value: 1, label: 'Низкий' },
      { value: 2, label: 'Средний' },
      { value: 3, label: 'Высокий' },
      { value: 4, label: 'Критичный' },
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
const fetchTasks = async () => {
  const workspaceId = route.params.id
  if (!workspaceId) return

  try {
    const response = await axios.get('/api/tasks/workspace', {
      params: { workspaceId: workspaceId }
    })
    tasks.value = response.data
  } catch (error) {
    console.error(`Ошибка при загрузке задач: ${error}`)
  }
}

onMounted(() => {
  fetchTasks()
})

const showSubmitModal = ref(false)
</script>