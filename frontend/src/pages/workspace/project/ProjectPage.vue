<template>
  <FrameWrapperComponent>
    <div class="object-wrapper-component">
      <div class="object-item-component">
        <FrameObjectContentDescriptionComponent
            :description="project?.description ?? ''"
          @save="updateProjectDescription"
        />
        <FrameObjectCommentsComponent
            :comments="projectComments"
            @add="handleAddComment"
            @delete="handleDeleteComment"
        />
        <DeleteModal
            :show="showDeleteModal"
            title="Удаление комментария"
            text="Вы уверены, что хотите удалить этот комментарий?"
            @close="closeDeleteModal"
            @confirm="confirmDeleteComment"
        />
      </div>
      <FrameObjectInfoPanelComponent
          class="object-info-panel"
          :fields="projectConfig"
          :data="project"
          @update:field="handleFieldUpdate"
      />
    </div>
  </FrameWrapperComponent>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import FrameWrapperComponent from "../../../components/frame/FrameWrapperComponent.vue"
import FrameObjectContentDescriptionComponent from '@/components/frame/FrameObjectContentDescriptionComponent.vue'
import FrameObjectCommentsComponent from '@/components/frame/FrameObjectCommentsComponent.vue'
import FrameObjectInfoPanelComponent from '@/components/frame/FrameObjectInfoPanelComponent.vue'
import DeleteModal from '@/components/modals/DeleteModal.vue'

import { buildCommentTree, addCommentToTree } from '@/utils/commentUtils'

import type { PanelFieldConfig } from '@/types/panel'
import type { Project } from '@/types/project'
import type { Comment } from '@/types/comment'
import { useAuthStore } from '@/stores/auth'
import {deleteCommentFromTree} from "../../../utils/commentUtils";
import { useToast } from '@/utils/ToastUtils'

const toast = useToast()
const authStore = useAuthStore()
const showDeleteModal = ref(false)
const projectComments = ref<Comment[]>([])
const currentCommentId = ref<number | null>(null)

const props = defineProps<{
  project: Project
}>()

//Панель информации
const projectConfig: PanelFieldConfig[] = [
  { key: 'name', label: 'Название', type: 'text' },
  { key: 'statusId', label: 'Статус', options: [
      { value: 1, label: 'В работе' },
      { value: 2, label: 'Приостановлен' },
      { value: 3, label: 'Завершен' },
      { value: 4, label: 'Отменен' },
      { value: 5, label: 'Новый' },
    ]},
  { key: 'deadlineDate', label: 'Дедлайн', type: 'date' },
  { key: 'completedAt', label: 'Дата завершения', type: 'date', readonly: true },
  { key: 'progress', label: 'Прогресс', type: 'progress-bar', from: 'totalTasksCount', to: 'completedTasksCount' },
  { key: 'createdAt', label: 'Создан', type: 'date', readonly: true },
  { key: 'updatedAt', label: 'Обновлен', type: 'date', readonly: true }
]
const handleFieldUpdate = async ({ key, value }: { key: string, value: any }) => {
  const oldValue = (props.project as any)[key]
  ;(props.project as any)[key] = value
  try {
    const dataToSend: Record<string, any> = {}

    if (key === 'deadlineDate') {
      const localDate = new Date(value + ':00')
      dataToSend[key] = localDate.toISOString()
    } else {
      dataToSend[key] = value
    }
    await axios.patch(`/api/projects/${props.project.id}`, dataToSend)
    toast.success('Поле успешно обновлено')
  } catch (error) {
    console.log(error)
    toast.error(`Ошибка`)
    ;(props.project as any)[key] = oldValue
  }
}
// Дескрипшн
const updateProjectDescription = async (newDescription: string) => {
  try {
    const response = await axios.patch(`/api/projects/${props.project.id}`, {
      description: newDescription
    })
    props.project.description = response.data.description
    toast.success('Описание успешно обновлено')
  } catch (error) {
    console.log(error)
    toast.error(`Ошибка`)
  }
}
// Комментарии
const handleDeleteComment = (id: number) => {
  currentCommentId.value = id
  showDeleteModal.value = true
}

const handleAddComment = async (text: string, parentId?: number ) => {
    try {
      const response = await axios.post(`/api/project-comments`, {
        projectId: props.project.id,
        message: text,
        replyId: parentId || null
      }, {
        params: {userId: authStore.userId}
      })
      addCommentToTree(projectComments.value, response.data, authStore.fullName || `Пользователь ${authStore.userId}`, parentId)
      toast.success('Комментарий успешно добавлен')
    } catch (error) {
      console.log(error)
      toast.error(`Ошибка`)
    }
}

const fetchComments = async() =>{
  try {
    const response = await axios.get(`/api/project-comments`, {
      params: { projectId: props.project.id }
    })
    projectComments.value = buildCommentTree(response.data)
  } catch (error) {
    console.log(error)
    toast.error(`Ошибка`)
  }
}

onMounted(() => {
  fetchComments()
})

const closeDeleteModal = () => {
  showDeleteModal.value = false
  currentCommentId.value = null
}
const confirmDeleteComment = async() => {
  if (currentCommentId.value === null) return
  try {
    const id = currentCommentId.value
    await axios.delete(`/api/project-comments/${id}`)
    deleteCommentFromTree(projectComments.value, id)
    closeDeleteModal()
    toast.success('Комментарий успешно удален')
  } catch (error) {
    console.log(error)
    toast.error(`Ошибка`)
  }
}

</script>