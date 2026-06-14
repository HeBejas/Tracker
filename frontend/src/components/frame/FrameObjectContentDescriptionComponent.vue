<template>
  <template v-if="!isEditing">
    <div class="description-container">
      <header class="frame-object-header">
        <h3>Описание</h3>
      </header>
      <textarea readonly class="description-textarea disabled" :class="{ 'is-empty': !description }">{{ description || 'Описание проекта' }}</textarea>
      <div class="description-actions">
        <RegularObjectBtn @click="startEditing">
          <template #icon>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
              <path d="M18.5 2.5a2.121 2.121 0 1 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
            </svg>
          </template>
          Изменить
        </RegularObjectBtn>
      </div>
    </div>
  </template>

  <template v-else>
    <div class="description-container">
      <header class="frame-object-header">
        <h3>Описание</h3>
      </header>
      <textarea
          ref="textareaRef"
          v-model="editableText"
          class="description-textarea"
      ></textarea>
      <div class="description-actions">
        <RegularObjectBtn @click="saveChanges">
          <template #icon>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="20 6 9 17 4 12"></polyline>
            </svg>
          </template>
          Сохранить
        </RegularObjectBtn>

        <RegularObjectBtn @click="cancelEditing">
          Отмена
        </RegularObjectBtn>
      </div>
    </div>
  </template>

</template>
<script setup lang="ts">
import { ref, nextTick } from 'vue'
import RegularObjectBtn from '@/components/buttons/RegularObjectBtn.vue'

interface Props {
  description?: string
}
const props = defineProps<Props>()

const emit = defineEmits<{
  (e: 'save', newText: string): void
}>()

const isEditing = ref(false)
const editableText = ref('')
const textareaRef = ref<HTMLTextAreaElement | null>(null)

const startEditing = async () => {
  editableText.value = props.description || ''
  isEditing.value = true
  await nextTick()
  if (textareaRef.value) {
    textareaRef.value.focus()
    const textLength = editableText.value.length
    textareaRef.value.setSelectionRange(textLength, textLength)
  }
}

const cancelEditing = () => {
  isEditing.value = false
}

const saveChanges = () => {
  const newText = editableText.value.trim()
  const oldText = (props.description || '').trim()

  if (newText !== oldText) {
    emit('save', newText)
  }

  isEditing.value = false
}
</script>


<style scoped>
.description-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 1rem;
  font-size: 1rem;
  width: 100%;
}
.description-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}
.description-textarea {
  width: 100%;
  box-sizing: border-box;
  resize: none;
  outline: none;
  field-sizing: content;
  min-height: 80px;
  padding: 12px 16px;
  border: 1px solid #e5e7eb;


  font-family: inherit;
  font-size: 15px;
  line-height: 1.6;
  color: #1f2937;
  field-sizing: content;

  border-radius: 8px;
  background: #ffffff;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  font-size: 1rem;
}
.description-textarea:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.description-textarea.is-empty {
  color: #9ca3af;
}

.description-textarea.disabled {
  cursor: default;
  //background: #f9fafb;
  color: #6b7280;
}


</style>