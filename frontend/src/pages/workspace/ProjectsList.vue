<template>
  <FrameWrapperComponent>
<!--    <FrameSimpleHeaderComponent>Проекты</FrameSimpleHeaderComponent>-->
    <FrameSimpleHeaderComponent>
      Проекты
      <template #actions>
        <CreateButton @click="showSubmitModal = true">
          Создать проект
        </CreateButton>
      </template>
    </FrameSimpleHeaderComponent>

      <TableComponent
          :columns="output_columns"
          :data="projects"
          @row-click="handleProjectClick"
      />
  </FrameWrapperComponent>
  <SubmitModal
      :show="showSubmitModal"
      title="Создать новый проект"
      text="Вы уверены что хотите создать новый проект?"
      submitLabel="Создать"
      @close="showSubmitModal = false"
      @submit="handleCreateNewProject"
  />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'
import FrameWrapperComponent from '../../components/frame/FrameWrapperComponent.vue'
import FrameSimpleHeaderComponent from '../../components/frame/FrameSimpleHeaderComponent.vue'
import TableComponent, { type TableColumn } from '../../components/tables/TableComponent.vue'
import CreateButton from '../../components/buttons/CreateBtn.vue'
import SubmitModal from '../../components/modals/SubmitModal.vue'
import type { Project } from '@/types/project'

const route = useRoute();
const router = useRouter()
const projects = ref<Project[]>([])
const authStore = useAuthStore()

const output_columns: TableColumn[] = [
  { key: 'name', label: 'Название', sortable: true, name_field: true },
  { key: 'statusId', label: 'Статус', filterable: true,
    options: [
      { value: 1, label: 'В работе' },
      { value: 2, label: 'Приостановлен' },
      { value: 3, label: 'Завершен' },
      { value: 4, label: 'Отменен' },
      { value: 5, label: 'Новый' },
    ]
  },
  { key: 'progress', label: 'Прогресс', type: 'progress-bar', from: 'totalTasksCount', to: 'completedTasksCount', sortable: false },
  { key: 'totalTasksCount', label: 'Всего задач', type: 'number', sortable: true, filterable: true },
  { key: 'completedTasksCount', label: 'Задач выполнено', type: 'number', sortable: true, filterable: true },
  { key: 'deadlineDate', label: 'Дедлайн', type: 'date', sortable: true, filterable: true },
  { key: 'createdAt', label: 'Создан', type: 'date', sortable: true, filterable: true }
]

const handleProjectClick = (project: Project) => {
  router.push({
    name: 'ProjectPage',
    params: { projectId: project.id },
    state: { projectName: project.name }
  })
}

const fetchProjects = async () => {
  const workspaceId = route.params.id;
  try{
    const response = await axios.get('/api/projects', {
      params: { workspaceId: workspaceId }
    })
    projects.value = response.data
  } catch (error) {
    console.error(`Ошибка при загрузке проектов: ${error}`);
  }
  console.log(projects.value)
}

onMounted(() => {
  fetchProjects()
})

const showSubmitModal = ref(false)

const handleCreateNewProject = async () => {
  const workspaceId = route.params?.id
  const authorId = authStore?.userId
  if (!authorId || !workspaceId) return
  try {
    const response = await axios.post('/api/projects',
        {
          name: 'Новый проект',
          workspaceId: workspaceId
        },
        {
          params: {
            authorId: authorId
          }
        }
    )
    const newProjectId = response.data.id
    handleProjectClick(response.data)
  } catch (error) {
    console.error('Не удалось создать проект:', error)
  }
}
</script>



