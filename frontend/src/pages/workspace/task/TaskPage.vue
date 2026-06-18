<template>
  <FrameWrapperComponent>
    <div class="object-wrapper-component">
      <div class="object-item-component">
        <FrameObjectContentDescriptionComponent
            :description="task?.description ?? ''"
            @save="updateTaskDescription"
        />
        <FrameObjectCommentsComponent
            :comments="taskComments"
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
          :fields="taskConfig"
          :data="task"
          :workspace-id="workspaceId"
          @update:field="handleFieldUpdate"
          @add:participant="handleAddParticipant"
          @remove:participant="handleRemoveParticipant"
      />
    </div>
  </FrameWrapperComponent>
</template>
<script setup lang="ts">
import { ref,onMounted } from 'vue'
import axios from 'axios'
import FrameWrapperComponent from "../../../components/frame/FrameWrapperComponent.vue"
import FrameObjectContentDescriptionComponent from '@/components/frame/FrameObjectContentDescriptionComponent.vue'
import FrameObjectCommentsComponent from '@/components/frame/FrameObjectCommentsComponent.vue'
import FrameObjectInfoPanelComponent from '@/components/frame/FrameObjectInfoPanelComponent.vue'
import DeleteModal from '@/components/modals/DeleteModal.vue'

import { useAuthStore } from '@/stores/auth'
import { useRoute } from 'vue-router'
import { currentTaskName } from '@/router'
import { buildCommentTree, addCommentToTree } from '@/utils/commentUtils'

import type { Task, TaskParticipant } from '@/types/task'
import type { Comment } from '@/types/comment'
import type { PanelFieldConfig } from '@/types/panel'
import {deleteCommentFromTree} from "../../../utils/commentUtils";
import { useToast } from '@/utils/ToastUtils'

const toast = useToast()
const authStore = useAuthStore()
const showDeleteModal = ref(false)
const taskComments = ref<Comment[]>([])
const currentCommentId = ref<number | null>(null)

const props = defineProps<{
  task: Task
}>()
const route = useRoute()
const workspaceId = Number(route.params.id) || 0

//Панель информации
const taskConfig: PanelFieldConfig[] = [
  { key: 'name', label: 'Название', type: 'text' },
  { key: 'projectName', label: 'Проект', type: 'text', sortable: true, filterable: true, readonly: true },
  {
    key: 'statusId',
    label: 'Статус',
    options: [
    { value: 1, label: 'Новая', colorClass: 'badge-new' },
    { value: 2, label: 'В работе', colorClass: 'badge-working' },
    { value: 3, label: 'На проверке', colorClass: 'badge-check' },
    { value: 4, label: 'Завершена', colorClass: 'badge-done' },
    { value: 5, label: 'Отменена', colorClass: 'badge-cancelled' },
  ]
  },
  {
    key: 'priorityId',
    label: 'Приоритет',
    options: [
      { value: 1, label: 'Низкий', colorClass: 'badge-white' },
      { value: 2, label: 'Средний', colorClass: 'badge-blue' },
      { value: 3, label: 'Высокий', colorClass: 'badge-red' },
      { value: 4, label: 'Критичный', colorClass: 'badge-yellow' },
    ]
  },
  { key: 'authorName', label: 'Автор', type: 'text', sortable: true, filterable: true, readonly: true },
  { key: 'participants', label: 'Исполнитель', type: 'participants' },
  { key: 'deadlineDate', label: 'Дедлайн', type: 'date' },
  { key: 'completedAt', label: 'Дата завершения', type: 'date', readonly: true },
  { key: 'createdAt', label: 'Создана', type: 'date', readonly: true },
  { key: 'updatedAt', label: 'Обновлена', type: 'date', readonly: true }
]

const handleFieldUpdate = async ({ key, value }: { key: string, value: any }) => {
  const oldValue = (props.task as any)[key]
  ;(props.task as any)[key] = value
  try {
    const dataToSend: Record<string, any> = {}

    if (key === 'deadlineDate') {
      const localDate = new Date(value + ':00')
      dataToSend[key] = localDate.toISOString()
    } else {
      dataToSend[key] = value
    }
    await axios.patch(`/api/tasks/${props.task.id}`, dataToSend)
    toast.success('Поле успешно обновлено')
  } catch (error) {
    console.log(error)
    toast.error(`Ошибка`)
    ;(props.task as any)[key] = oldValue
  }
}

// Дескрипшн
const updateTaskDescription = async (newDescription: string) => {
  console.log('Новое описание прилетело:', newDescription)
  try {
    const response = await axios.patch(`/api/tasks/${props.task.id}`, {
      description: newDescription
    })
    props.task.description = response.data.description
    toast.success('Описание успешно обновлено')
  } catch (error) {
    console.log(error)
    toast.error(`Ошибка`)
  }
}


//Комментарии
const handleDeleteComment = (id: number) => {
  currentCommentId.value = id
  showDeleteModal.value = true
}

const handleAddComment = async (text: string, parentId?: number ) => {
  try{
    const response = await axios.post(`/api/task-comments`, {
      taskId: props.task.id,
      message: text,
      replyId: parentId || null
    }, {
      params: { userId: authStore.userId }
    })
    addCommentToTree(taskComments.value, response.data, authStore.fullName || `Пользователь ${authStore.userId}`, parentId)
    toast.success('Описание успешно обновлено')
  } catch (error) {
    console.log(error)
    toast.error(`Ошибка`)
  }
}

const fetchComments = async() =>{
  try {
    const response = await axios.get(`/api/task-comments`, {
      params: { taskId: props.task.id }
    })
    taskComments.value = buildCommentTree(response.data)
  } catch (error) {
    console.log(error)
    toast.error(`Ошибка`)
  }
}

const confirmDeleteComment = async() => {
  if (currentCommentId.value === null) return
  try {
    const id = currentCommentId.value
    await axios.delete(`/api/task-comments/${id}`)
    deleteCommentFromTree(taskComments.value, id)
    closeDeleteModal()
    toast.success('Комментарий успешно удален')
  } catch (error) {
    console.log(error)
    toast.error(`Ошибка`)
  }
}

const closeDeleteModal = () => {
  showDeleteModal.value = false
  currentCommentId.value = null
}

const handleAddParticipant = async ({ userId, roleId }: { userId: number; roleId: number }) => {
  try {
    const response = await axios.post(`/api/tasks/${props.task.id}/participants`, {
      userId,
      roleId
    })
    const newParticipant: TaskParticipant = response.data
    if (!props.task.participants) {
      props.task.participants = []
    }
    props.task.participants.push(newParticipant)
    toast.success('Исполнитель добавлен')
  } catch (error) {
    console.error(error)
    toast.error('Ошибка при добавлении исполнителя')
  }
}

const handleRemoveParticipant = async (participant: TaskParticipant) => {
  try {
    await axios.delete(`/api/tasks/${props.task.id}/participants`, {
      data: {
        userId: participant.userId,
        roleId: participant.roleId
      }
    })
    if (props.task.participants) {
      props.task.participants = props.task.participants.filter(
        p => p.userId !== participant.userId || p.roleId !== participant.roleId
      )
    }
    toast.success('Исполнитель удален')
  } catch (error) {
    console.error(error)
    toast.error('Ошибка при удалении исполнителя')
  }
}

onMounted(() => {
  currentTaskName.value = props.task.name
  fetchComments()
  console.log(props.task)
})
</script>