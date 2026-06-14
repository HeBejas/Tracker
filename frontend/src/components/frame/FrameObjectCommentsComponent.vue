<template>
  <div class="comments-section">
    <header class="frame-object-header">
      <h3>Комментарии ({{ totalComments }})</h3>
    </header>
    <CommentsInputComponent
        @submit="text => $emit('add', text)"
    />
    <CommentsListComponent
        :comments="comments"
        @add-reply="(text, parentId) => $emit('add', text, parentId)"
        @delete="id => $emit('delete', id)"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Comment } from '@/types/comment'
import RegularObjectBtn from '@/components/buttons/RegularObjectBtn.vue'
import CommentsInputComponent from '@/components/comments/CommentsInputComponent.vue'
import CommentsListComponent from '@/components/comments/CommentsListComponent.vue'

const props = defineProps<{
  comments: Comment[]
}>()

const emit = defineEmits<{
  (e: 'add', text: string, parentId?: number): void
  (e: 'delete', id: number): void
}>()

const totalComments = computed(() => {
  return props.comments.reduce((acc, curr) => acc + 1 + (curr.replies?.length || 0), 0)
})

</script>

<style scoped>
.comments-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
}

.comments-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

/* --- СТИЛИ РЕДАКТОРА (КАК НА СКРИНШОТЕ) --- */


/* --- СТИЛИ СПИСКА КОММЕНТАРИЕВ --- */

</style>