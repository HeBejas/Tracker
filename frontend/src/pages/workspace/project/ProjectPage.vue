<template>
  <div class="object-wrapper-component">

    <div class="object-item-component">
      <FrameObjectContentComponent />
      <FrameObjectCommentsComponent />
    </div>
    <FrameObjectInfoPanelComponent />

  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { Project } from '@/types/project'

defineProps<{
  project: Project
}>()
</script>