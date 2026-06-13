<template>
  <div class="input-container">
    <div class="input-comment-wrapper">
      <div class="editor-toolbar">
        <div class="toolbar-group">
          <button class="toolbar-btn" title="Отменить">↩</button>
          <button class="toolbar-btn" title="Повторить">↪</button>
        </div>
        <div class="toolbar-divider"></div>
        <div class="toolbar-group">
          <button class="toolbar-btn font-bold">B</button>
          <button class="toolbar-btn font-italic">i</button>
          <button class="toolbar-btn font-underline">U</button>
          <button class="toolbar-btn font-strike">S</button>
        </div>
        <div class="toolbar-divider"></div>
        <div class="toolbar-group">
          <button class="toolbar-btn">H</button>
          <button class="toolbar-btn">≡</button>
        </div>
      </div>

      <textarea
          ref="textareaRef"
          v-model="newCommentText"
          class="editor-textarea"
          :placeholder="placeholder"
      ></textarea>
    </div>

    <div class="input-comment-actions">
      <button
          v-if="showCancel"
          class="cancel-btn"
          @click="$emit('cancel')"
      >
        Отмена
      </button>

      <RegularObjectBtn :disabled="!newCommentText.trim()" @click="submitComment">
        {{ submitLabel }}
      </RegularObjectBtn>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import RegularObjectBtn from '@/components/buttons/RegularObjectBtn.vue'

interface Props {
  placeholder?: string
  submitLabel?: string
  showCancel?: boolean
}

withDefaults(defineProps<Props>(), {
  placeholder: 'Напишите комментарий',
  submitLabel: 'Отправить',
  showCancel: false
})

const emit = defineEmits<{
  (e: 'submit', text: string): void
  (e: 'cancel'): void
}>()

const newCommentText = ref('')
const textareaRef = ref<HTMLTextAreaElement | null>(null)

const submitComment = () => {
  if (!newCommentText.value.trim()) return
  emit('submit', newCommentText.value.trim())
  newCommentText.value = ''
}

const focus = () => {
  textareaRef.value?.focus()
}
defineExpose({ focus })
</script>

<style scoped>
.input-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
}
.input-comment-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 12px;
}
.cancel-btn {
  background: transparent;
  color: #6b7280;
  border: none;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
}
.cancel-btn:hover {
  color: #374151;
}
.input-comment-wrapper {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #ffffff;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}
.input-comment-wrapper:focus-within {
  border-color: #3b82f6;
  box-shadow: 0 0 0 1px #3b82f6;
}
.editor-toolbar {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
  gap: 8px;
  flex-wrap: wrap;
}
.toolbar-group { display: flex; gap: 4px; }
.toolbar-divider { width: 1px; height: 20px; background: #d1d5db; margin: 0 4px; }
.toolbar-btn { background: none; border: none; border-radius: 4px; padding: 4px 8px; color: #4b5563; cursor: pointer; font-size: 14px; transition: background 0.2s; }
.toolbar-btn:hover { background: #e5e7eb; }
.font-bold { font-weight: bold; }
.font-italic { font-style: italic; }
.font-underline { text-decoration: underline; }
.font-strike { text-decoration: line-through; }
.editor-textarea { width: 100%; min-height: 100px; padding: 12px 16px; border: none; resize: none; outline: none; font-family: inherit; font-size: 15px; line-height: 1.5; color: #1f2937; field-sizing: content; }
</style>