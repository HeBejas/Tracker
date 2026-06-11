<template>
  <button class="regular-object-btn" type="button">
    <span v-if="$slots.icon" class="btn-icon">
      <slot name="icon" />
    </span>
    <span class="btn-text">
      <slot />
    </span>
  </button>
</template>

<script setup lang="ts">
</script>

<style scoped>
.regular-object-btn {
  font-size: 0.9rem;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  background-color: transparent;
  border: 1px solid transparent;
  border-radius: 6px;
  color: #606266;
  font-family: inherit;
  font-weight: 500;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  width: fit-content;
}

.regular-object-btn:hover {
  background-color: #f4f4f5;
  color: #303133;
}

.regular-object-btn:active {
  background-color: #e9e9eb;
  transform: scale(0.98);
}

.btn-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  color: currentColor;
}

.btn-text {
  line-height: 1.4;
}
</style>