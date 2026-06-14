<template>
  {{task}}
</template>
<script setup lang="ts">
import { onMounted } from 'vue'
import axios from 'axios'
import { useRoute } from 'vue-router'
import { currentTaskName } from '@/router'

import type { Task } from '@/types/task'


const props = defineProps<{
  task: Task
}>()


const route = useRoute()

onMounted(() => {
  currentTaskName.value = props.task.name
})
</script>