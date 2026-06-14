<template>
  <div class="settings-page">
    <header class="frame-object-header">
      <h3>Удалить задачу</h3>
    </header>
    <section class="settings-card danger-zone">
      <div class="card-body danger-content">
        <div class="danger-text">
          <p>После удаления задачи все её подзадачи, комментарии и файлы будут удалены навсегда. Это действие нельзя отменить.</p>
        </div>
        <button class="btn-danger" @click="showDeleteModal=true">Удалить задачу</button>
      </div>
    </section>
  </div>
  <DeleteModal
      :show="showDeleteModal"
      title="Удаление задачи"
      :text="`Вы уверены, что хотите удалить задачу «${props.task?.name}»?`"
      @close="showDeleteModal = false"
      @confirm="confirmDeleteTask"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import DeleteModal from '@/components/modals/DeleteModal.vue'
import { useToast } from '@/utils/ToastUtils'
import type { Task } from '@/types/task'

const toast = useToast()
const router = useRouter()
const route = useRoute()

const props = defineProps<{
  task: Task
}>()

const showDeleteModal = ref(false)

const confirmDeleteTask = async () => {
  try {
    await axios.delete(`/api/tasks/${props.task.id}`)
    toast.success('Задача успешно удалена')
    showDeleteModal.value = false
    router.push({
      name: 'ProjectTasks',
      params: {
        id: route.params.id,
        projectId: route.params.projectId
      }
    })
  } catch (error) {
    console.log(error)
    toast.error('Ошибка при удалении задачи')
    showDeleteModal.value = false
  }
}
</script>

<style scoped>
.settings-page {
  margin: 0 auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  animation: fadeIn 0.4s ease-out;
}

.settings-header h2 {
  margin: 0 0 8px 0;
  font-size: 1.5rem;
  color: #111827;
  font-weight: 600;
}

.settings-header p {
  margin: 0;
  color: #6b7280;
  font-size: 0.95rem;
}

.settings-card {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.card-header {
  padding: 20px 24px;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
}

.card-header h3 {
  margin: 0;
  font-size: 1.1rem;
  color: #374151;
  font-weight: 600;
}

.card-body {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #374151;
}

.form-group input,
.form-group textarea {
  padding: 10px 14px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 0.95rem;
  color: #1f2937;
  transition: border-color 0.2s, box-shadow 0.2s;
  font-family: inherit;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  padding-top: 8px;
}

/* Кнопки */
button {
  cursor: pointer;
  font-family: inherit;
  font-weight: 500;
  transition: all 0.2s;
}

.btn-primary {
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 0.95rem;
}

.btn-primary:hover:not(:disabled) {
  background-color: #1d4ed8;
  transform: translateY(-1px);
}

.btn-primary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* Опасная зона */
.danger-zone {
  border-color: #fca5a5;
}

.danger-zone .card-header {
  background: #fef2f2;
  border-bottom-color: #fca5a5;
}

.danger-zone .card-header h3 {
  color: #b91c1c;
}

.danger-content {
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.danger-text h4 {
  margin: 0 0 4px 0;
  font-size: 1rem;
  color: #111827;
}

.danger-text p {
  margin: 0;
  font-size: 0.9rem;
  color: #6b7280;
}

.btn-danger {
  background-color: #ef4444;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 0.95rem;
  white-space: nowrap;
}

.btn-danger:hover {
  background-color: #dc2626;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 640px) {
  .danger-content {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>