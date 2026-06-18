<template>
  <div class="info-panel">
    <div v-for="field in fields" :key="field.key" class="info-row">
      <!-- Название поля -->
      <div class="info-label">
        {{ field.label }}
      </div>

      <!-- Значение поля -->
      <div class="info-value-wrapper">

        <!-- РЕЖИМ РЕДАКТИРОВАНИЯ -->
        <template v-if="editingField === field.key && !field.readonly && field.type !== 'participants'">
          <div class="edit-mode-container">
            <!-- Select -->
            <select
                v-if="field.options"
                v-model="editValue"
                @blur="saveEdit(field)"
                @change="saveEdit(field)"
                @keydown.esc="cancelEdit"
                v-focus
                class="pro-input pro-select"
            >
              <option v-for="opt in field.options" :key="opt.value" :value="opt.value">
                {{ opt.label }}
              </option>
            </select>
            <!-- Date -->
            <input
                v-else-if="field.type === 'date'"
                type="datetime-local"
                v-model="editValue"
                @blur="saveEdit(field)"
                @keydown.enter="saveEdit(field)"
                @keydown.esc="cancelEdit"
                v-focus
                class="pro-input"
            />
            <!-- Text / Number  -->
            <input
                v-else
                :type="field.type === 'number' ? 'number' : 'text'"
                v-model="editValue"
                @blur="saveEdit(field)"
                @keydown.enter="saveEdit(field)"
                @keydown.esc="cancelEdit"
                v-focus
                class="pro-input"
                :placeholder="`Введите ${field.label.toLowerCase()}...`"
            />
          </div>
        </template>
        <!-- РЕЖИМ ПРОСМОТРА -->
        <template v-else>
          <div
              class="view-mode-container"
              :class="{ 'is-editable': !field.readonly && field.type !== 'progress-bar' && field.type !== 'participants' }"
              @click="startEditing(field)"
          >
            <!-- Компонент для выбора участников -->
            <SelectUserComponent
                v-if="field.type === 'participants'"
                :workspace-id="workspaceId || 0"
                :participants="data.participants || []"
                @add:participant="handleAddParticipant"
                @remove:participant="handleRemoveParticipant"
            />

            <!-- Остальное содержимое -->
            <div v-else class="content-render">
              <div v-if="field.type === 'progress-bar'" class="progress-wrapper">
                <div class="progress-text">{{ data[field.to || ''] || 0 }} / {{ data[field.from || ''] || 0 }}</div>
                <div class="progress-track">
                  <div class="progress-fill" :style="{ width: calculateProgress(data[field.to || ''], data[field.from || '']) + '%' }"></div>
                </div>
              </div>

              <span 
                v-else-if="field.options" 
                class="badge" 
                :class="field.options.find(opt => opt.value === data[field.key])?.colorClass || 'badge-gray'"
              >
                {{ getOptionLabel(field, data[field.key]) }}
              </span>

              <span v-else-if="field.type === 'date'" class="text-value" :class="{'text-empty': !data[field.key]}">
                {{ formatDate(data[field.key]) }}
              </span>

              <span v-else class="text-value" :class="{'text-empty': !data[field.key]}">
                {{ formatValue(data[field.key]) }}
              </span>
            </div>

            <svg v-if="!field.readonly && field.type !== 'progress-bar' && field.type !== 'participants'" class="edit-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
              <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
            </svg>
          </div>
        </template>

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { PanelFieldConfig } from '@/types/panel'
import type { TaskParticipant } from '@/types/task'
import { formatUIDate } from '@/utils/dateUtils'
import SelectUserComponent from '@/components/inputs/SelectUserComponent.vue'


const props = defineProps<{
  fields: PanelFieldConfig[]
  data: Record<string, any>
  workspaceId?: number
}>()

const emit = defineEmits<{
  (e: 'update:field', payload: { key: string, value: any }): void
  (e: 'add:participant', payload: { userId: number, roleId: number }): void
  (e: 'remove:participant', payload: TaskParticipant): void
}>()

const editingField = ref<string | null>(null)
const editValue = ref<any>(null)

const vFocus = {
  mounted: (el: HTMLElement) => el.focus()
}

const startEditing = (field: PanelFieldConfig) => {
  if (field.readonly || field.type === 'progress-bar' || field.type === 'participants') return

  editingField.value = field.key
  
  if (field.type === 'date') {
    let dateObj: Date;
    
    if (props.data[field.key]) {
      dateObj = new Date(props.data[field.key]);
    } else {
      dateObj = new Date(); 
    }
    if (!isNaN(dateObj.getTime())) {
      const tzOffset = dateObj.getTimezoneOffset() * 60000;
      const localISOTime = new Date(dateObj.getTime() - tzOffset).toISOString().slice(0, 16);
      editValue.value = localISOTime;
    } else {
      editValue.value = null;
    }
    
  } else {
    editValue.value = props.data[field.key]
  }
}

const handleAddParticipant = ({ userId, roleId }: { userId: number; roleId: number }) => {
  emit('add:participant', { userId, roleId })
}

const handleRemoveParticipant = (participant: TaskParticipant) => {
  emit('remove:participant', participant)
}

const cancelEdit = () => {
  editingField.value = null
  editValue.value = null
}

const saveEdit = (field: PanelFieldConfig) => {
  if (editingField.value === field.key) {
    
    let valueToEmit = editValue.value;
    let hasChanged = false;

    if (field.type === 'date') {
      if (!valueToEmit) {
        valueToEmit = null;
        hasChanged = props.data[field.key] != null;
      } else {
        let originalFormatted = null;
        
        if (props.data[field.key]) {
          const d = new Date(props.data[field.key]);
          if (!isNaN(d.getTime())) {
            const tzOffset = d.getTimezoneOffset() * 60000;
            originalFormatted = new Date(d.getTime() - tzOffset).toISOString().slice(0, 16);
          }
        }
        
        hasChanged = valueToEmit !== originalFormatted;
      }
    } else {
      hasChanged = valueToEmit !== props.data[field.key];
    }

    if (hasChanged) {
      emit('update:field', { key: field.key, value: valueToEmit })
    }
    
    editingField.value = null
  }
}

// Хелперы
const getOptionLabel = (field: PanelFieldConfig, value: any): string => {
  if (value === null || value === undefined) return '—'
  const option = field.options?.find(opt => opt.value === value)
  return option ? option.label : String(value)
}

const formatValue = (value: any): string => {
  if (value === null || value === undefined || value === '') return 'Пусто'
  return String(value)
}

const formatDate = (dateString: string | null): string => {
  if (!dateString) return 'Не указана'
  return formatUIDate(dateString) || 'Не указана'
}

const calculateProgress = (completed: number, total: number) => {
  if (!total) return 0
  return Math.min(100, Math.round((completed / total) * 100))
}
</script>

<style scoped>
/* .info-panel {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 20px;
  background: #ffffff;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
} */

.info-panel {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 24px;
  background: #ffffff;
  border-radius: 16px;
  border: 1px solid #f3f4f6;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.03), 0 2px 4px -1px rgba(0, 0, 0, 0.02), inset 0 0 0 1px rgba(255, 255, 255, 0.6);
}

.info-row {
  display: flex;
  align-items: flex-start;
  min-height: 28px;
}

 .info-label {
  width: 40%;
  color: #6b7280;
  font-weight: 500;
  font-size: 1rem;
  letter-spacing: -0.03em;
  /* align-self: center; */
} 

.info-value-wrapper {
  width: 65%;
  position: relative;
  display: flex;
  /* align-items: center; */
  /* height: stretch; */
  /* align-self: center; */
}
/* --- Режим просмотра --- */
.view-mode-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  border-radius: 8px;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.view-mode-container.is-editable {
  cursor: pointer;
}

.view-mode-container.is-editable:hover {
  background-color: #f3f4f6;
}
.view-mode-container.is-editable {
  cursor: pointer;
}

.view-mode-container.is-editable:hover {
  background-color: #f9fafb;
  border-color: #e5e7eb;
}

.content-render {
  flex: 1;
  overflow: hidden;
}

.edit-icon {
  width: 14px;
  height: 14px;
  color: #9ca3af;
  opacity: 0;
  transform: translateX(-4px);
  transition: all 0.2s ease;
}

.view-mode-container.is-editable:hover .edit-icon {
  opacity: 1;
  transform: translateX(0);
  color: #6b7280;
}

/* --- Режим редактирования--- */
.edit-mode-container {
  margin-left: -8px;
}

.pro-input {
  width: 100%;
  padding: 4px 8px;
  font-size: 14px;
  color: #111827;
  background-color: #ffffff;
  border: 1px solid #3b82f6;
  border-radius: 6px;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
  outline: none;
  font-family: inherit;
  transition: box-shadow 0.2s;
  box-sizing: border-box;
}

.pro-select {
  cursor: pointer;
  appearance: none;
  background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
  background-repeat: no-repeat;
  background-position: right 8px center;
  background-size: 14px;
  padding-right: 28px;
}

/* --- Стили значений --- */
.text-value {
  font-size: 0.9rem;
  align-self: center;
  color: #111827;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block;
}

.text-empty {
  color: #9ca3af;
  font-style: italic;
}

/* Прогресс-бар */
.progress-wrapper {
  display: flex;
  flex-direction: column; /* Меняем на колонку, чтобы текст был над баром */
  align-items: flex-start;
  gap: 5px;
  width: 100%;
  max-width: 130px; /* Ограничиваем ширину как во втором компоненте */
}

.progress-text {
  font-size: 12px;
  color: #4b5563;
  font-weight: 500;
  white-space: nowrap;
}

.progress-track {
  width: 100%;
  height: 8px; /* Увеличили высоту до 8px как во втором */
  background-color: #e5e7eb;
  border-radius: 4px; /* Скругление */
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: #3b82f6; /* Синий цвет как во втором */
  border-radius: 4px;
  transition: width 0.4s ease;
}

</style>