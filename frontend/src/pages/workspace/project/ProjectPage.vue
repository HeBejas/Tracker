<template>
  <div class="object-wrapper-component">

    <div class="object-item-component">
      <FrameObjectContentDescriptionComponent
        :description = project?.description
        @save="updateProjectDescription"
      />
      <FrameObjectCommentsComponent />
    </div>
    <FrameObjectInfoPanelComponent />

  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import FrameObjectContentDescriptionComponent from '@/components/frame/FrameObjectContentDescriptionComponent.vue'
import type { Project } from '@/types/project'

const props = defineProps<{
  project: Project
}>()

const updateProjectDescription = async (newDescription: string) => {
  console.log('Новое описание прилетело:', newDescription)
  try {
    const response = await axios.patch(`/api/projects/${props.project.id}`, {
      description: newDescription
    })
    props.project.description = response.data.description

  } catch (error) {
    console.error('Ошибка при сохранении:', error)
  }
}
</script>