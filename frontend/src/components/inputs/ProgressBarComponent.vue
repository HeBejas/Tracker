<template>
  <div class="progress-wrapper">
    <div class="progress-text">
      {{ data[field.to || ''] || 0 }} / {{ data[field.from || ''] || 0 }}
    </div>
    
    <div class="progress-track">
      <div 
        class="progress-fill" 
        :style="{ width: calculateProgress(data[field.to || ''], data[field.from || '']) + '%' }"
      ></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { PanelFieldConfig } from '@/types/panel'

defineProps<{
  field: PanelFieldConfig
  data: Record<string, any>
}>()

// Твоя оригинальная функция расчета процентов
const calculateProgress = (completed: number, total: number) => {
  if (!total) return 0
  return Math.min(100, Math.round((completed / total) * 100))
}
</script>

<style scoped>
.progress-wrapper {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 5px;
  width: 100%;
  max-width: 130px;
}

.progress-text {
  font-size: 12px;
  color: #4b5563;
  font-weight: 500;
  white-space: nowrap;
}

.progress-track {
  width: 100%;
  height: 8px;
  background-color: #e5e7eb;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: #3b82f6;
  border-radius: 4px;
  transition: width 0.4s ease;
}
</style>