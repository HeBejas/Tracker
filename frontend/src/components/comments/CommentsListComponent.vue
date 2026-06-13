<template>
  <div class="comments-list">
    <div v-for="comment in comments" :key="comment.id" class="comment-thread">

      <div class="comment-item">
        <div class="comment-avatar">{{ comment.author.name.charAt(0) }}</div>
        <div class="comment-content">
          <div class="comment-header">
            <span class="author-name">{{ comment.author.name }}</span>
            <span class="comment-time">{{ formatUIDate(comment.createdAt) }}</span>
          </div>
          <div class="comment-text">{{ comment.text }}</div>
          <div class="comment-actions">
            <button @click="openReplyEditor(comment.id)">Ответить</button>
            <button class="delete-text" @click="$emit('delete', comment.id)">Удалить</button>
          </div>
        </div>
      </div>

      <div v-if="replyingToId === comment.id" class="reply-editor-container">
        <CommentsInputComponent
            ref="replyInputRef"
            placeholder="Напишите ответ..."
            submit-label="Ответить"
            show-cancel
            @submit="text => submitReply(text, comment.id)"
            @cancel="closeReplyEditor"
        />
      </div>

      <div v-if="comment.replies && comment.replies.length" class="replies-list">
        <div v-for="reply in comment.replies" :key="reply.id" class="comment-item reply-item">
          <div class="comment-avatar small">{{ reply.author.name.charAt(0) }}</div>
          <div class="comment-content">
            <div class="comment-header">
              <span class="author-name">{{ reply.author.name }}</span>
              <span class="comment-time">{{ formatUIDate(comment.createdAt) }}</span>
            </div>
            <div class="comment-text">{{ reply.text }}</div>
            <div class="comment-actions">
              <button @click="openReplyEditor(comment.id)">Ответить</button>
              <button
                  v-if="checkDeletePermission(reply.author.id)"
                  class="delete-text"
                  @click="$emit('delete', reply.id)">
                Удалить
              </button>
              
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import type { Comment } from '@/types/comment'
import CommentsInputComponent from '@/components/comments/CommentsInputComponent.vue'
import { formatUIDate } from '@/utils/dateUtils'
import { useAuthStore } from '@/stores/auth'

defineProps<{
  comments: Comment[]
}>()

const emit = defineEmits<{
  (e: 'add-reply', text: string, parentId: number): void
  (e: 'delete', id: number): void
}>()

const authStore = useAuthStore()
const replyingToId = ref<number | null>(null)

const replyInputRef = ref<InstanceType<typeof CommentsInputComponent>[]>([])

const openReplyEditor = async (parentId: number) => {
  replyingToId.value = parentId
  await nextTick()

  if (replyInputRef.value && replyInputRef.value.length > 0) {
    const activeEditor = replyInputRef.value.find(comp => comp != null)

    if (activeEditor) {
      activeEditor.focus()
    }
  }
}

const closeReplyEditor = () => {
  replyingToId.value = null
}

const submitReply = (text: string, parentId: number) => {
  emit('add-reply', text, parentId)
  closeReplyEditor()
}

const checkDeletePermission = (authorId: number): boolean => {
  const hasModRole = authStore.userRole === 'admin' || authStore.userRole === 'manager'
  const isAuthor = authStore.userId === authorId
  return hasModRole || isAuthor
}
</script>

<style scoped>
.comments-list { display: flex; flex-direction: column; gap: 24px; }
.comment-thread { display: flex; flex-direction: column; gap: 16px; }
.comment-item { display: flex; gap: 16px; }
.comment-avatar { width: 40px; height: 40px; border-radius: 50%; background: #e5e7eb; color: #4b5563; display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 16px; flex-shrink: 0; }
.comment-avatar.small { width: 32px; height: 32px; font-size: 14px; }
.comment-content { flex-grow: 1; display: flex; flex-direction: column; gap: 4px; }
.comment-header { display: flex; align-items: baseline; gap: 8px; }
.author-name { font-weight: 600; color: #111827; font-size: 15px; }
.comment-time { font-size: 13px; color: #6b7280; }
.comment-text { color: #374151; font-size: 15px; line-height: 1.5; white-space: pre-wrap; }
.comment-actions { display: flex; gap: 12px; margin-top: 4px; }
.comment-actions button { background: none; border: none; color: #6b7280; font-size: 13px; cursor: pointer; padding: 0; font-weight: 500; }
.comment-actions button:hover { color: #374151; }
.comment-actions .delete-text:hover { color: #ef4444; }
.replies-list { display: flex; flex-direction: column; gap: 16px; margin-left: 56px; }

.reply-editor-container {
  margin-left: 56px;
}
</style>