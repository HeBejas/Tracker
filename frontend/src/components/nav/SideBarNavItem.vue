<template>
  <component
      :is="to ? RouterLink : 'div'"
      :to="to"
      class="sidebar-item"
      :class="{ 'is-collapsed': isCollapsed }"
      @click="$emit('click')"
  >
    <div class="icon-wrapper">
      <slot name="icon"></slot>
    </div>

    <span v-show="!isCollapsed" class="text-content">
      <slot></slot>
    </span>
  </component>
</template>

<script setup>
import { RouterLink } from 'vue-router'

defineProps({
  to: {
    type: String,
    default: null
  },
  isCollapsed: {
    type: Boolean,
    default: false
  }
})

defineEmits(['click'])
</script>

<style scoped>
.sidebar-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
  margin: 2px 12px;
  border-radius: 6px;
  cursor: pointer;
  color: #333;
  font-size: 14px;
  text-decoration: none;
  transition: background-color 0.2s, color 0.2s;
  white-space: nowrap;
}

.sidebar-item:hover {
  background-color: #f0f0f0;
}

/* Активный класс, который vue-router вешает автоматически на текущую страницу */
.sidebar-item.router-link-active {
  background-color: #eef2ff;
  color: #3c50e0;
  font-weight: 500;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  color: inherit;
  flex-shrink: 0;
}

.is-collapsed {
  justify-content: center;
  padding: 8px 0;
  margin: 2px 8px;
}
</style>