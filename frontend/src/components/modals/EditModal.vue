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
<!--          <option value="" disabled>Выберите...</option>-->
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
      <ModalPrimaryBtn
          type="submit"
          form="dynamic-form"
          :disabled="!isChanged"
      >
        {{ submitLabel }}
      </ModalPrimaryBtn>
    </template>

  </ModalOverlay>
</template>

<script setup lang="ts">
import { reactive, watch, ref, computed } from 'vue'
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
  data: { type: Object, default: () => ({}) }
})

const emit = defineEmits<{
  close: []
  submit: [data: Record<string, any>]
}>()

const formData = reactive<Record<string, any>>({})
const baselineData = ref<Record<string, any>>({})

const isChanged = computed(() => {
  return props.fields.some(field => {
    const currentValue = formData[field.key]
    const baselineValue = baselineData.value[field.key]
    return String(currentValue ?? '') !== String(baselineValue ?? '')
  })
})

const initForm = () => {
  Object.keys(formData).forEach(key => delete formData[key])
  const newBaseline: Record<string, any> = {}

  props.fields.forEach(field => {
    const rawValue = props.data[field.key]
    if (field.type === 'select') {
      let matchedValue: any = ''

      if (rawValue !== undefined && rawValue !== null) {
        const rawStr = String(rawValue).trim().toLowerCase()
        const found = field.options?.find(opt => String(opt.value).toLowerCase() === rawStr)
        matchedValue = found ? found.value : rawValue
      }

      formData[field.key] = matchedValue
      newBaseline[field.key] = matchedValue

    } else if (field.type === 'number') {
      const num = Number(rawValue)
      formData[field.key] = isNaN(num) ? 0 : num
      newBaseline[field.key] = formData[field.key]

    } else {
      formData[field.key] = rawValue !== undefined && rawValue !== null ? rawValue : ''
      newBaseline[field.key] = formData[field.key]
    }
  })

  baselineData.value = newBaseline
}

watch(() => props.show, (newVal) => {
  if (newVal) {
    initForm()
  }
})

watch(() => props.data, () => {
  if (props.show) {
    initForm()
  }
}, { deep: true })

const onSubmit = () => {
  if (!isChanged.value) return
  emit('submit', { ...formData })
}
</script>

<style scoped>
.modal-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 8px 0;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #334155;
  letter-spacing: 0.02em;
}

.form-input {
  padding: 10px 14px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 14px;
  color: #0f172a;
  background-color: #ffffff;
  outline: none;
  transition: all 0.2s ease;
  font-family: inherit;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02) inset;
}

.form-input:hover {
  border-color: #94a3b8;
}

.form-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.form-input:disabled {
  background-color: #f8fafc;
  color: #94a3b8;
  cursor: not-allowed;
}

select.form-input {
  cursor: pointer;
}
</style>

