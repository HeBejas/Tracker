<template>
  <button
      v-if="text"
      :class="{active: isActive}"
  >
    {{ text }}
  </button>
</template>
<script setup>
defineProps({
  text: { type: String, default: '' },
  isActive: Boolean
})
</script>

<style scoped>
button{
  padding: 10px 4px;
  position: relative;

  color: var(--second-text-color);
  font-size: var(--default-font-size);
  font-weight: 500;
  border: none;
  background: none;
  cursor: pointer;
  transition: color 0.2s;
}
button.active{
  color: #2563eb;
  font-weight: 600;
}
button.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #2563eb;
}
</style>