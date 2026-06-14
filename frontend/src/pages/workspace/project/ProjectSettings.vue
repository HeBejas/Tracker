<template>
  <div class="settings-page">
    <header class="frame-object-header">
      <h3>Удалить проект</h3>
    </header>

    <section class="settings-card danger-zone">
      <div class="card-body danger-content">
        <div class="danger-text">
          <p>После удаления проекта все его задачи, комментарии и файлы будут удалены навсегда. Это действие нельзя отменить.</p>
        </div>
        <button class="btn-danger" @click="showDeleteModal=true">Удалить проект</button>
      </div>
    </section>
  </div>

  <DeleteModal
      :show="showDeleteModal"
      title="Удаление проекта"
      :text="`Вы уверены, что хотите удалить проект «${props.project?.name}»?`"
      @close="showDeleteModal = false"
      @confirm="confirmDeleteProject"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import DeleteModal from '@/components/modals/DeleteModal.vue'
import { useToast } from '@/utils/ToastUtils'
import type { Project } from '@/types/project'

const toast = useToast()
const router = useRouter()
const route = useRoute()

// Принимаем project из родительского ProjectLayout
const props = defineProps<{
  project: Project
}>()

const showDeleteModal = ref(false)

const confirmDeleteProject = async () => {
  if (!props.project?.id) return

  try {
    await axios.delete(`/api/projects/${props.project.id}`)
    toast.success('Проект успешно удален')

    showDeleteModal.value = false

    // Возвращаемся к списку проектов текущей рабочей среды (Workspace)
    router.push({
      name: 'ProjectsList',
      params: {
        id: route.params.id // ID рабочей среды берем из URL
      }
    })
  } catch (error) {
    console.log(error)
    toast.error('Ошибка при удалении проекта')
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

.frame-object-header h3 {
  margin: 0 0 8px 0;
  font-size: 1.5rem;
  color: #111827;
  font-weight: 600;
}

.settings-card {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.card-body {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Опасная зона */
.danger-zone {
  border-color: #fca5a5;
}

.danger-content {
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.danger-text p {
  margin: 0;
  font-size: 0.95rem;
  color: #4b5563;
  line-height: 1.5;
}

.btn-danger {
  background-color: #ef4444;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 500;
  white-space: nowrap;
  cursor: pointer;
  transition: all 0.2s;
  font-family: inherit;
}

.btn-danger:hover {
  background-color: #dc2626;
  transform: translateY(-1px);
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

  .btn-danger {
    width: 100%;
  }
}
</style>