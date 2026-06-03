А-а-а! Сука, так если в запросе на бэк должно уходить именно **число** (`1` или `2`), тогда всё встает на свои места!

Вот почему у тебя было **пусто**:

1. Бэк на чтение отдал строку `"archived"`.
2. Модалка открылась в тот момент, когда данные (`props.data`) ещё не успели долететь (асинхронность во Vue). Ватч отработал по пустому объекту, выставил везде пустоту, а когда данные через миллисекунду пришли — селект уже не перерисовывался.
3. Плюс бэк на запись ждет строго число, а мы пытались переделать конфиг на строки.

**Всё, раунд. Мы их дожали.** Я переписал код модалки так, чтобы тебе **НЕ нужно было менять свой конфиг полей** (оставляй там числа `1` и `2`).

Что теперь делает этот код:

1. **Жестко следит за асинхронностью:** Если данные формы прилетят чуть позже открытия модалки, ватч это поймает и мгновенно заполнит поля. Никаких пустых статусов при открытии больше не будет.
2. **Умный импорт:** При открытии он видит строку `"archived"` от бэка и превращает её внутри формы в число `2` (или `"active"` в `1`), чтобы Vue выставил правильный статус.
3. **Чистый экспорт:** При отправке (`onSubmit`) форма отдает наружу **чистые числа** (`1` или `2`), ровно так, как требует твой бэкенд.

Забирай этот вариант, просто полностью замени им код модалки:

```vue
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

// Проверка изменений (приводим к строке, чтобы типы 2 и "2" не взрывали логику)
const isChanged = computed(() => {
  return props.fields.some(field => {
    const currentValue = formData[field.key]
    const baselineValue = baselineData.value[field.key]
    return String(currentValue ?? '') !== String(baselineValue ?? '')
  })
})

// Хелпер безопасного поиска ключей (включая проверку на _id)
const getRawValue = (key: string, data: any) => {
  if (!data) return undefined
  if (data[key] !== undefined) return data[key]

  if (key.endsWith('_id')) {
    const shortKey = key.replace('_id', '')
    if (data[shortKey] !== undefined) return data[shortKey]
  }

  const longKey = `${key}_id`
  if (data[longKey] !== undefined) return data[longKey]

  return undefined
}

// Главная функция сборки данных формы
const initForm = () => {
  Object.keys(formData).forEach(key => delete formData[key])
  const newBaseline: Record<string, any> = {}

  props.fields.forEach(field => {
    const rawValue = getRawValue(field.key, props.data)

    if (field.type === 'select') {
      let matchedValue: any = ''

      if (rawValue !== undefined && rawValue !== null) {
        const rawStr = String(rawValue).trim().toLowerCase()

        // Маппим строковые энамы бэка в числа вашего фронтового конфига fields
        if (rawStr === 'active') {
          matchedValue = 1 // Переводим строку бэка в число 1 для селекта
        } else if (rawStr === 'archived' || rawStr === 'archive') {
          matchedValue = 2 // Переводим строку бэка в число 2 для селекта
        } else {
          // Если пришло нормальное число, ищем совпадение в опциях
          const found = field.options?.find(opt => String(opt.value) === rawStr)
          matchedValue = found ? found.value : rawValue
        }
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

// Срабатывает при физическом открытии модалки
watch(() => props.show, (newVal) => {
  if (newVal) {
    initForm()
  }
})

// Срабатывает, если данные строки (props.data) долетели асинхронно чуть позже открытия
watch(() => props.data, () => {
  if (props.show) {
    initForm()
  }
}, { deep: true })

const onSubmit = () => {
  if (!isChanged.value) return

  // Отправляем наружу чистый formData, в котором лежат выбранные числа (1 или 2)
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

