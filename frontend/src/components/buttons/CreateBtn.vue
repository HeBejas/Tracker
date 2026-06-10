<script setup lang="ts">
</script>
<template>
  <button class="create-button" @click="$emit('click')">
    <slot />
  </button>
</template>

<style scoped>
.create-button {
  bottom: 40px;
  right: 40px;
  background: linear-gradient(135deg, #2563eb, #3b82f6);
  color: white;
  padding: 14px 32px;
  border-radius: 10px;
  border: none;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 10px 15px -3px rgba(37, 99, 235, 0.3);
  transition: transform 0.2s, box-shadow 0.2s;
}

.create-button:hover {
  transform: scale(1.05);
  box-shadow: 0 15px 20px -3px rgba(37, 99, 235, 0.4);
}
</style>