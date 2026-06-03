<template>
  <component
      :is="resolvedTo ? 'router-link' : 'div'"
      :to="resolvedTo"
      class="nav-item"
      :exact-active-class="noActive ? '' : 'active'"
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
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const props = defineProps({
  to: {
    type: String,
    default: null
  },
  noActive: {
    type: Boolean,
    default: false
  }
})

const route = useRoute()

// Автоматически лечим проблему с ":id".
// Если в пути есть двоеточие, заменяем его на реальный ID из текущего URL
const resolvedTo = computed(() => {
  if (!props.to) return null
  let path = props.to

  Object.keys(route.params).forEach(key => {
    path = path.replace(`:${key}`, route.params[key])
  })

  return path
})
</script>

<style scoped>
.nav-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #64748b; /* Сделали текст чуть мягче (современный slate-цвет) */
  font-size: 14px;
  padding: 6px 12px;
  border-radius: 6px;
  transition: all 0.2s ease;
  user-select: none;
  text-decoration: none; /* Убираем дефолтное подчеркивание ссылок */
  font-weight: 500;
}

.nav-item:hover {
  background-color: #f1f5f9;
  color: #0f172a;
}

/* Красивое состояние активной вкладки/ссылки */
.nav-item.active {
  background-color: #e0f2fe; /* Нежно-голубой фон */
  color: #0369a1;            /* Насыщенный синий текст */
  font-weight: 600;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  color: currentColor; /* Иконка автоматически красится в цвет текста */
}

.text-content {
  line-height: 1;
}

/* Оставил твои стили для дропдауна на случай, если пригодятся */
.is-dropdown {
  border: 1px solid #e2e8f0;
  background-color: white;
  padding: 4px 10px;
}
</style>