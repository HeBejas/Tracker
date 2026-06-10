<template>
  <FrameWrapperComponent v-if="project">

    <FrameSimpleHeaderComponent>{{ project.name }}</FrameSimpleHeaderComponent>
    <FrameHeaderNavComponent />

     <FrameContentWrapperComponent>
        <router-view
            :project="project"
            :state="{ projectName: project.name }"
        />
     </FrameContentWrapperComponent>

  </FrameWrapperComponent>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import FrameWrapperComponent from "../../../components/frame/FrameWrapperComponent.vue"
import FrameSimpleHeaderComponent from "../../../components/frame/FrameSimpleHeaderComponent.vue"
import FrameHeaderNavComponent from '../../../components/frame/FrameHeaderNavComponent.vue'
import type { Project } from '@/types/project'

const route = useRoute()
const router = useRouter()
const project = ref<Project>()

const fetchProject = async () => {
  try {
    const projectId = route.params.projectId
    const response = await axios.get(`api/projects/${projectId}`)
    project.value = response.data
    setProjectNameInMeta(response.data.name)
  } catch (error) {
    console.error(`Ошибка загрузки проекта: ${error}`)
  }
}
onMounted(() => {
  fetchProject()
})

const setProjectNameInMeta = (name: string) => {
  const parentRoute = route.matched.find(r => r.path.includes(':projectId'))
  if (parentRoute) {
    parentRoute.meta.projectName = name
  }
}

</script>

<!--finally {-->
<!--isLoading.value = false-->
<!--}-->
<!--const isLoading = ref(true)-->
<!--<div v-if="isLoading" class="loader">-->
<!--Загрузка проекта...-->
<!--</div>-->