<template>
  <FrameWrapperComponent>
    <FrameSimpleHeaderComponent>Проекты</FrameSimpleHeaderComponent>
<!--    <FrameContentComponent>-->
      <TableComponent
          :columns="output_columns"
          :data="projects"
          @row-click="handleProjectClick"
      />

<!--    </FrameContentComponent>-->
  </FrameWrapperComponent>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router';
import axios from 'axios'
import FrameWrapperComponent from '../../components/frame/FrameWrapperComponent.vue'
import FrameSimpleHeaderComponent from '../../components/frame/FrameSimpleHeaderComponent.vue'
import TableComponent, { type TableColumn } from '../../components/tables/TableComponent.vue'

interface Project{
  id: number
  workspace_id: number
  status_id: number
  name: string
  description: string | null
  start_date: string | null
  end_date: string | null
  total_tasks_count: number
  completed_tasks_count: number
  created_at: string
}

const output_columns: TableColumn[] = [
  { key: 'name', label: 'Название', sortable: true, name_field: true },
  { key: 'statusId', label: 'Статус', filterable: true,
    options: [
      { value: 1, label: 'В работе' },
      { value: 2, label: 'Приостановлен' },
      { value: 3, label: 'Завершен' },
      { value: 4, label: 'Отменен' }
    ]
  },
  { key: 'progress', label: 'Прогресс', type:'progress-bar', from:'totalTasksCount', to:'completedTasksCount', sortable: false},
  { key: 'totalTasksCount', label: 'Всего задач', type:'number',sortable: true, filterable: true  },
  { key: 'completedTasksCount', label: 'Задач выполнено', type:'number', sortable: true, filterable: true  },
  // { key: 'startDate', label: 'Дата начала',type:'date' , sortable: true },
  { key: 'endDate', label: 'Дата окончания', type:'date' ,sortable: true, filterable: true  },
  { key: 'createdAt', label: 'Создан', type:'date', sortable: true, filterable: true  }
]

const route = useRoute();
const projects = ref<Project[]>([])

const handleProjectClick = (project: Project) => {
  router.push(`/projects/${project.id}`)
}

const fetchProjects = async () => {
  const workspaceId = route.params.id;
  try{
    const response = await axios.get('/api/projects', {
      params: { workspaceId: workspaceId }
    })
    projects.value = response.data
  } catch (error) {
    console.error('Ошибка при загрузке проектов:', error);
  }
  console.log(projects.value)
}
onMounted(() => {
  fetchProjects()
})

</script>



