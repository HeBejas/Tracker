<script setup lang="ts">
import { ref } from 'vue'
import type { TableColumn } from '@/components/tables/TableComponent.vue'

const props = defineProps<{
  column: TableColumn
  modelValue: any
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: any): void
}>()

const textValue = ref(props.modelValue || '')
const checkedOptions = ref<any[]>(Array.isArray(props.modelValue) ? [...props.modelValue] : [])
const dateValue = ref(props.modelValue?.date || '')
const dateMode = ref(props.modelValue?.mode || 'exact')
const numValue = ref(props.modelValue?.value ?? '')
const numMode = ref(props.modelValue?.mode || 'exact')

const emitUpdate = () => {
  if (props.column.options) {
    emit('update:modelValue', checkedOptions.value.length ? checkedOptions.value : null)
  } else if (props.column.type === 'date') {
    emit('update:modelValue', dateValue.value ? { mode: dateMode.value, date: dateValue.value } : null)
  } else if (props.column.type === 'number') {
    emit('update:modelValue', (numValue.value !== '' && numValue.value !== null) ? { mode: numMode.value, value: numValue.value } : null)
  } else {
    emit('update:modelValue', textValue.value || null)
  }
}
</script>

<template>
  <div class="filter-window" @click.stop>

    <div v-if="column.options" class="checkbox-list">
      <label v-for="opt in column.options" :key="opt.value" class="checkbox-item">
        <input
            type="checkbox"
            :value="opt.value"
            v-model="checkedOptions"
            @change="emitUpdate"
        />
        <span>{{ opt.label }}</span>
      </label>
    </div>

    <div v-else-if="column.type === 'date'" class="filter-group">
      <select v-model="dateMode" @change="emitUpdate" class="control-element">
        <option value="exact">Точная дата</option>
        <option value="from">От (>=)</option>
        <option value="to">До (<=)</option>
      </select>
      <input
          type="date"
          v-model="dateValue"
          @change="emitUpdate"
          class="control-element"
      />
    </div>

    <div v-else-if="column.type === 'number'" class="filter-group">
      <select v-model="numMode" @change="emitUpdate" class="control-element">
        <option value="exact">Точно</option>
        <option value="from">От (>=)</option>
        <option value="to">До (<=)</option>
      </select>
      <input
          type="number"
          v-model="numValue"
          @input="emitUpdate"
          class="control-element"
          placeholder="Введите число"
      />
    </div>

    <div v-else class="filter-group">
      <input
          type="text"
          v-model="textValue"
          @input="emitUpdate"
          :placeholder="`Поиск по: ${column.label}`"
          class="control-element"
      />
    </div>

  </div>
</template>

<style scoped>
.filter-window {
  position: absolute;
  top: calc(100% - 8px);
  left: 0;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  min-width: 220px;
  z-index: 100;
  /* Теперь завязано на класс управления */
  visibility: hidden;
  opacity: 0;
  transform: translateY(-5px);
  transition: opacity 0.15s ease, transform 0.15s ease;
}

/* Окно становится видимым, когда родитель вешает этот класс */
.filter-window.is-visible {
  visibility: visible;
  opacity: 1;
  transform: translateY(0);
}

.checkbox-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.checkbox-item {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 13px;
  color: #334155;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.control-element {
  box-sizing: border-box;
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 13px;
  outline: none;
  background: #fff;
  transition: border-color 0.2s;
}

.control-element:focus {
  border-color: #4f46e5;
}
</style>