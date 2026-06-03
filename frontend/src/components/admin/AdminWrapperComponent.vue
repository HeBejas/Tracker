<template>
  <div class="admin-wrapper">
    <div class="page">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.admin-wrapper {
  background: #ffffff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  border: 1px solid #e2e8f0;
}
.page{
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
