<template>
  <div v-if="!isEditing" class="view-mode-container" :class="{ 'is-editable': !field.readonly }" @click="startEditing">
    <div class="content-render">
      <span class="text-value" :class="{'text-empty': !modelValue}">
        {{ formatValue(modelValue) }}
      </span>
    </div>
    <svg v-if="!field.readonly" class="edit-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
      <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
    </svg>
  </div>

  <div v-else class="edit-mode-container">
    <input
      :type="field.type === 'number' ? 'number' : 'text'"
      v-model="editValue"
      @blur="saveEdit"
      @keydown.enter="saveEdit"
      @keydown.esc="cancelEdit"
      v-focus
      class="pro-input"
      :placeholder="`Введите ${field.label.toLowerCase()}...`"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { PanelFieldConfig } from '@/types/panel'

const props = defineProps<{ field: PanelFieldConfig; modelValue: any }>()
const emit = defineEmits(['update:modelValue'])

const isEditing = ref(false)
const editValue = ref<any>(null)

const vFocus = { mounted: (el: HTMLElement) => el.focus() }

const formatValue = (val: any) => (val === null || val === undefined || val === '') ? 'Пусто' : String(val)

const startEditing = () => {
  if (props.field.readonly) return
  editValue.value = props.modelValue
  isEditing.value = true
}

const saveEdit = () => {
  if (editValue.value !== props.modelValue) {
    emit('update:modelValue', editValue.value)
  }
  isEditing.value = false
}

const cancelEdit = () => {
  isEditing.value = false
}
</script>
    