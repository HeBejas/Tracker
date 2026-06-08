<template>
  <ModalOverlay :show="show" :title="title" @close="$emit('close')">

    <form id="dynamic-form" @submit.prevent="onSubmit" class="modal-form">
      <div class="form-group" v-for="field in fields" :key="field.key">
        <label :for="field.key">{{ field.label }}</label>

        <input
            v-if="field.type === 'text' || field.type === 'number'"
            :id="field.key"
            :type="field.type"
            v-model="formData[field.key]"
            :placeholder="field.placeholder || ''"
            class="form-input"
        />

        <select
            v-else-if="field.type === 'select'"
            :id="field.key"
            v-model="formData[field.key]"
            class="form-input"
        >
          <option value="" disabled>Выберите...</option>
          <option
              v-for="option in field.options"
              :key="option.value"
              :value="option.value"
          >
            {{ option.label }}
          </option>
        </select>
      </div>
    </form>

    <template #footer>
      <ModalSecondaryBtn @click="$emit('close')">Отмена</ModalSecondaryBtn>
      <ModalPrimaryBtn type="submit" form="dynamic-form">{{ submitLabel }}</ModalPrimaryBtn>
    </template>

  </ModalOverlay>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue'
import ModalOverlay from "./ModalOverlay.vue"
import ModalPrimaryBtn from "./modals_buttons/ModalPrimaryBtn.vue"
import ModalSecondaryBtn from "./modals_buttons/ModalSecondaryBtn.vue"

export interface FormField {
  key: string
  label: string
  type: 'text' | 'number' | 'select'
  placeholder?: string
  options?: { value: string | number, label: string }[]
}

const props = defineProps({
  show: { type: Boolean, required: true },
  title: { type: String, required: true },
  fields: { type: Array as () => FormField[], default: () => [] },
  submitLabel: { type: String, default: 'Сохранить' },
  initialData: { type: Object, default: () => ({}) }
})

const emit = defineEmits<{
  close: []
  submit: [data: Record<string, any>]
}>()

const formData = reactive<Record<string, any>>({})

watch(() => props.show, (newVal) => {
  if (newVal) {
    Object.keys(formData).forEach(key => delete formData[key])

    if (props.initialData) {
      Object.assign(formData, props.initialData)
    }

    props.fields.forEach(field => {
      if (!(field.key in formData)) {
        formData[field.key] = field.type === 'number' ? 0 : ''
      }
    })
  }
})

const onSubmit = () => {
  emit('submit', { ...formData })
}
</script>
