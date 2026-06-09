<template>
  <router-view :workspace-id="Number(route.params.id)" />
</template>

<script setup>
import { useRoute } from 'vue-router'
const route = useRoute()
</script>