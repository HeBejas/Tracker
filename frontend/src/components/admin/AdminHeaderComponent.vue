<template>
  <div class="admin-header">
    <h2>{{ title }}</h2>
      <slot />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string
}>()
</script>

<style scoped>
  .admin-header{
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .page-header h2 {
    margin: 0;
    font-size: 1.25rem;
    color: #0f172a;
  }
</style>


<!--.header-actions h2 { margin: 0; font-size: 1.5rem; color: #0f172a; }-->




