<template>
  <component
      :is="to ? 'router-link' : 'div'"
      :to="to"
      class="nav-item"
  >

      <div v-if="$slots.icon" class="icon-wrapper">
        <slot name="icon"></slot>
      </div>

      <span v-if="$slots.default" class="text-content">
        <slot></slot>
      </span>

  </component>
</template>

<script setup>
defineProps({
  to: {
    type: String,
    default: null
  }
})
</script>

<style scoped>
.nav-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #333;
  font-size: 13px;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.2s;
  user-select: none;
}

.nav-item:hover {
  background-color: #f0f0f0;
}

.is-dropdown {
  border: 1px solid #ddd;
  background-color: white;
  padding: 4px 10px;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  color: #555;
}

.text-content {
  line-height: 1;
}

.chevron {
  margin-left: 2px;
}
</style>