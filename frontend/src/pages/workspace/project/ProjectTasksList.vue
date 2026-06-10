<template>
  123123
  {{project}}
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { Project } from '@/types/project'

defineProps<{
  project: Project
}>()
</script>