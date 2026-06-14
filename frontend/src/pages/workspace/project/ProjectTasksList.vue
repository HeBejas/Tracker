<template>
  <FrameWrapperComponent>
    <TableComponent
        :columns="output_columns"
        :data="tasks"
        @row-click="handleTaskClick"
    >
      <template #action>
        <CreateButton @click="showSubmitModal = true">
          Создать задачу
        </CreateButton>
      </template>
    </TableComponent>
  </FrameWrapperComponent>
  <SubmitModal
      :show="showSubmitModal"
      title="Создать задачу"
      :text="`Вы уверены что хотите создать новую задачу для &quot;${project.name}&quot; ?`"
      submitLabel="Создать"
      @close="showSubmitModal = false"
      @submit="handleCreateNewTask"
  />

</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import FrameWrapperComponent from '@/components/frame/FrameWrapperComponent.vue'
import FrameHeaderComponent from '@/components/frame/FrameHeaderComponent.vue'
import TableComponent, { type TableColumn } from '@/components/tables/TableComponent.vue'
import CreateButton from '@/components/buttons/CreateBtn.vue'
import SubmitModal from '@/components/modals/SubmitModal.vue'
import { useAuthStore } from '@/stores/auth'
import type { Task } from '@/types/task'
import type { Project } from '@/types/project'

const props = defineProps<{
  project: Project
}>()

const route = useRoute();
const router = useRouter()
const authStore = useAuthStore()

const tasks = ref<Task[]>([])
const showSubmitModal = ref(false)


const output_columns: TableColumn[] = [
  { key: 'name', label: 'Название', sortable: true, name_field: true },
  {
    key: 'statusId',
    label: 'Статус',
    filterable: true,
    options: [
      { value: 1, label: 'Новая' },
      { value: 2, label: 'В работе' },
      { value: 3, label: 'На проверке' },
      { value: 4, label: 'Завершена' }
    ]
  },
  {
    key: 'priority_id',
    label: 'Приоритет',
    sortable: true,
    filterable: true,
    options: [
      { value: 1, label: 'Низкий' },
      { value: 2, label: 'Средний' },
      { value: 3, label: 'Высокий' }
    ]
  },
  { key: 'authorId', label: 'Автор', type: 'number', sortable: true, filterable: false },
  // { key: 'authorName', label: 'Автор', type: 'text', sortable: true, filterable: true },
  { key: 'deadlineDate', label: 'Дедлайн', type: 'date', sortable: true, filterable: true },
  { key: 'completedAt', label: 'Дата завершения', type: 'date', sortable: true, filterable: true },
  { key: 'createdAt', label: 'Создана', type: 'date', sortable: true, filterable: true },
  { key: 'updatedAt', label: 'Обновлена', type: 'date', sortable: true, filterable: true  }
]

const handleTaskClick = (task: Task) => {
  console.log(`Переход к задаче ${task}`)
  router.push({
    name: 'ProjectTaskPage',
    params: { taskId: task.id },
    state: { taskName: task.name }
  })
}

const fetchTasks = async () => {
  try{
    const response = await axios.get('/api/tasks', {
      params: { projectId: props.project.id }
    })
    tasks.value = response.data
  } catch (error) {
    console.error(`Ошибка при загрузке задач: ${error}`);
  }
}

onMounted(() => {
  fetchTasks()
})

const handleCreateNewTask = async () => {
  const authorId = authStore?.userId
  if (!authorId) return
  try {
    const response = await axios.post('/api/tasks',
        {
          name: 'Новая задача',
          projectId: props.project.id,
          priority: 'low',
          status: 'new'
        },
        {
          params: {
            authorId: authorId
          }
        }
    )
    const newProjectId = response.data.id
    handleTaskClick(response.data)
  } catch (error) {
    console.error(`Не удалось создать задачу: ${error}`)
  }
}
</script>




