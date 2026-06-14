<template>
  <FrameWrapperComponent v-if="task">
    <FrameHeaderComponent>{{ task.name }}</FrameHeaderComponent>
    <FrameHeaderNavComponent />
    <router-view
        :task="task"
        :state="{ taskName: task.name }"
    />
  </FrameWrapperComponent>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import FrameHeaderNavComponent from "../../../components/frame/FrameHeaderNavComponent.vue";
import FrameHeaderComponent from "../../../components/frame/FrameHeaderComponent.vue";
import FrameWrapperComponent from "../../../components/frame/FrameWrapperComponent.vue";
import { currentProjectName } from '@/router/index'
import { currentTaskName } from '@/router/index'

const route = useRoute()
const router = useRouter()
const task = ref<Project>()

const fetchTask = async () => {
  try {
    const taskId = route.params.taskId
    const response = await axios.get(`api/tasks/${taskId}`)
    task.value = response.data
    currentTaskName.value = response.data.name
  } catch (error) {
    console.error(`Ошибка загрузки задачи: ${error}`)
  }
}
const fetchProjectName = async () => {
  const projectId = route.params.projectId

  if (projectId && !currentProjectName.value) {
    try {
      const { data } = await axios.get(`/api/projects/${projectId}`)
      currentProjectName.value = data.name
    } catch (e) {
      console.error("Не удалось подгрузить имя проекта")
    }
  }
}
onMounted(() => {
  fetchProjectName()
  fetchTask()
})
</script>