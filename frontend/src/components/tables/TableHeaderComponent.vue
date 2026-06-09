<template>
  <div class="tracker-header">

    <div class="search-section">
      <input
          type="text"
          :value="searchQuery"
          @input="emit('update:search', ($event.target as HTMLInputElement).value)"
          placeholder="Поиск по названию"
          class="main-search-input"
      />
    </div>

    <div class="clouds-container">
      <div
          v-for="key in activeFilterKeys"
          :key="key"
          class="filter-cloud-wrapper"
          @mouseenter="hoveredKey = key"
          @mouseleave="hoveredKey = null"
      >
        <div class="filter-cloud" :class="{ 'has-value': activeFilters[key] }">
          <span class="cloud-label">{{ getCloudText(key) }}</span>
          <button class="cloud-close" @click.stop="removeFilterCloud(key)">×</button>
        </div>

        <FilterWindow
            :column="columns.find(c => c.key === key)!"
            :model-value="activeFilters[key]"
            :class="{ 'is-visible': hoveredKey === key }"
            @update:model-value="emit('update-filter', key, $event)"
        />
      </div>

      <div
          class="add-filter-wrapper"
          v-if="availableColumns.length > 0"
          @mouseenter="isAddMenuOpen = true"
          @mouseleave="isAddMenuOpen = false"
      >
        <button class="add-btn">
          <span class="plus-icon">+</span>
          Фильтр
        </button>

        <div class="add-filter-dropdown" :class="{ 'is-visible': isAddMenuOpen }">
          <div
              v-for="col in availableColumns"
              :key="col.key"
              class="dropdown-item"
              @click.stop="addFilterCloud(col.key)"
          >
            {{ col.label }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { TableColumn } from './TableComponent.vue'
import FilterWindow from './filter/FilterWindow.vue'

const props = defineProps<{
  columns: TableColumn[]
  activeFilters: Record<string, any>
  searchQuery: string
}>()

const emit = defineEmits<{
  (e: 'update-filter', key: string, value: any): void
  (e: 'remove-filter', key: string): void
  (e: 'update:search', value: string): void
}>()

// Переменные состояния для контроля окон
const hoveredKey = ref<string | null>(null)
const isAddMenuOpen = ref(false)
const activeFilterKeys = ref<string[]>([])

const nameColumn = computed(() => props.columns.find(c => c.name_field))

const availableColumns = computed(() => {
  return (props.columns || []).filter(
      col => col.filterable && !activeFilterKeys.value.includes(col.key)
  )
})

const addFilterCloud = (key: string) => {
  if (!activeFilterKeys.value.includes(key)) {
    activeFilterKeys.value.push(key)
  }
  // Мгновенно схлопываем выпадашку после клика, чтобы избежать залипания
  isAddMenuOpen.value = false
}

const removeFilterCloud = (key: string) => {
  activeFilterKeys.value = activeFilterKeys.value.filter(k => k !== key)
  emit('remove-filter', key)
  hoveredKey.value = null // Сбрасываем фокус при удалении
}

const getCloudText = (key: string) => {
  const col = props.columns.find(c => c.key === key)
  if (!col) return key

  const val = props.activeFilters[key]
  if (!val) return col.label

  let valStr = ''
  if (col.options && Array.isArray(val)) {
    const selectedLabels = col.options.filter(o => val.includes(o.value)).map(o => o.label)
    if (selectedLabels.length) valStr = selectedLabels.join(', ')
  } else if (col.type === 'date' || col.type === 'number') {
    const modeVal = col.type === 'date' ? val.date : val.value
    if (modeVal) {
      const prefix = val.mode === 'from' ? 'От ' : val.mode === 'to' ? 'До ' : ''
      valStr = prefix + modeVal
    }
  } else if (typeof val === 'string') {
    valStr = val
  }

  return valStr ? `${col.label}: ${valStr}` : col.label
}
</script>

<style scoped>
.tracker-header {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.main-search-input {
  box-sizing: border-box;
  width: 100%;
  max-width: 400px;
  padding: 8px 14px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
}
.main-search-input:focus {
  border-color: #4f46e5;
}

.clouds-container {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
}

.filter-cloud-wrapper {
  position: relative;
  padding-bottom: 10px;
  margin-bottom: -10px;
}

/* Пробиваем скоуп для FilterWindow. Важно: используем класс, а не :hover */
:deep(.filter-window.is-visible) {
  visibility: visible !important;
  opacity: 1 !important;
  transform: translateY(0) !important;
}

.filter-cloud {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px 6px 16px;
  background: #f1f5f9;
  color: #475569;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 500;
  cursor: default;
  transition: all 0.2s;
}

.filter-cloud.has-value {
  background: linear-gradient(135deg, #4f46e5 0%, #0ea5e9 100%);
  color: white;
  /* Меняем transparent на цвет начала градиента, чтобы скрыть грязь на углах */
  border-color: white;
  box-shadow: 0 2px 6px rgba(79, 70, 229, 0.25);
}
.cloud-close {
  background: transparent;
  border: none;
  color: inherit;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
  line-height: 1;
  opacity: 0.7;
  transition: opacity 0.2s, background 0.2s;
}
.filter-cloud.has-value .cloud-close:hover {
  background: rgba(255, 255, 255, 0.2);
  opacity: 1;
}
.filter-cloud:not(.has-value) .cloud-close:hover {
  background: #e2e8f0;
  opacity: 1;
}

.add-filter-wrapper {
  position: relative;
  padding-bottom: 10px;
  margin-bottom: -10px;
}

.add-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 5px 12px 5px 8px;
  background: white;
  border: 1px dashed #cbd5e1;
  color: #64748b;
  border-radius: 999px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}
.add-btn:hover {
  border-color: #4f46e5;
  color: #4f46e5;
}

.plus-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  background: #f1f5f9;
  border-radius: 50%;
  color: #475569;
  font-weight: bold;
  font-size: 14px;
  line-height: 1;
}
.add-btn:hover .plus-icon {
  background: #e0e7ff;
  color: #4f46e5;
}

.add-filter-dropdown {
  position: absolute;
  top: calc(100% - 8px);
  left: 0;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 8px 0;
  min-width: 180px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  z-index: 100;
  /* Управление через класс */
  visibility: hidden;
  opacity: 0;
  transform: translateY(-5px);
  transition: opacity 0.15s ease, transform 0.15s ease;
}

.add-filter-dropdown.is-visible {
  visibility: visible;
  opacity: 1;
  transform: translateY(0);
}

.dropdown-item {
  padding: 8px 16px;
  font-size: 13px;
  color: #334155;
  cursor: pointer;
}
.dropdown-item:hover {
  background: #f8fafc;
  color: #4f46e5;
}
</style>