<template>
  <div class="dashboard-container">
    <div class="projects-block">
      <ProjectsListComponent
          :projects="projects"
          @select="navigateToProject"
          @create="showCreateProjectModal = true"
      />
    </div>

    <div class="tabs-block">
      <DashboardTabComponent
          :tabs="dashboardTabs"
          @select="handleTabSelect"
      />
    </div>

    <div class="tasks-block">
      <MyTasksListComponent
          :tasks="myTasks"
          @select="navigateToTask"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import ProjectsListComponent from '@/components/dashboard/ProjectsListComponent.vue'
import MyTasksListComponent from '@/components/dashboard/MyTasksListComponent.vue'
import DashboardTabComponent from '@/components/dashboard/DashboardTabComponent.vue'
import { useAuthStore } from '@/stores/auth'
import type { Project } from '@/types/project'
import type { Task } from '@/types/task'

interface DashboardTab {
  id: number
  name: string
  icon: string
}

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const workspaceId = Number(route.params.id) || 0

const projects = ref<Project[]>([])
const myTasks = ref<Task[]>([])
const showCreateProjectModal = ref(false)

const dashboardTabs: DashboardTab[] = [
  { id: 1, name: 'Статистика', icon: '/icons/dashboard-stats.png' },
  { id: 2, name: 'Календарь', icon: '/icons/dashboard-calendar.png' },
  { id: 3, name: 'Отчеты', icon: '/icons/dashboard-reports.png' },
  { id: 4, name: 'Команда', icon: '/icons/dashboard-team.png' }
]

const fetchProjects = async () => {
  try {
    const response = await axios.get(`/api/projects`, {
      params: { workspaceId }
    })
    projects.value = response.data
  } catch (error) {
    console.error('Ошибка при загрузке проектов:', error)
  }
}

const fetchMyTasks = async () => {
  try {
    const response = await axios.get(`/api/tasks/my`, {
      params: { 
        workspaceId,
        userId: authStore.userId 
      }
    })
    myTasks.value = response.data
  } catch (error) {
    console.error('Ошибка при загрузке задач:', error)
  }
}

const navigateToProject = (project: Project) => {
  router.push(`/workspaces/${workspaceId}/projects/${project.id}`)
}

const navigateToTask = (task: Task) => {
  router.push(`/workspaces/${workspaceId}/projects/${task.projectId}/tasks/${task.id}`)
}

const handleTabSelect = (tabId: number) => {
  console.log('Выбрана вкладка:', tabId)
}

onMounted(() => {
  fetchProjects()
  fetchMyTasks()
})
</script>

<style scoped>
.dashboard-container {
  display: grid;
  grid-template-rows: 2fr 1fr; 
  gap: 1.5rem;
  /* height: 85vh;  */
  background: #f5f6f8;
  box-sizing: border-box;
}

.projects-block {
  grid-column: 1 / 2;
  grid-row: 1 / 2;
  overflow: hidden;
}

.tabs-block {
  grid-column: 1 / 2;
  grid-row: 2 / 3;
}

.tasks-block {
  grid-column: 2 / 3;
  grid-row: 1 / 3; 
  overflow: hidden;
}

@media (max-width: 1200px) {
  .dashboard-container {
    display: flex;
    flex-direction: column;
    height: auto;
    overflow-y: auto;
  }
  
  .projects-block, .tasks-block {
    height: 400px; 
  }

  .tabs-block {
    height: auto;
    min-height: 200px;
  }
}
</style>