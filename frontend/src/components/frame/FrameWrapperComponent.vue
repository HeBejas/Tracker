<template>
  <div class="frame-wrapper">
    <div class="frame">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.frame-wrapper {
  padding-top: 1.5rem;
}
.frame{
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
</style>
