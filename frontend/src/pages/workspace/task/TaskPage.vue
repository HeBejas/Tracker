<template>
  <FrameWrapperComponent>
    <div class="object-wrapper-component">
      <div class="object-item-component">
        <FrameObjectContentDescriptionComponent
            :description = task?.description
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
          @update:field="handleFieldUpdate"
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


import type { Task } from '@/types/task'
import type { Comment } from '@/types/comment'
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

//Панель информации
const taskConfig: PanelFieldConfig[] = [
  { key: 'name', label: 'Название', type: 'text' },
  { key: 'projectName', label: 'Проект', type: 'text', sortable: true, filterable: true, readonly: true },
  {
    key: 'statusId',
    label: 'Статус',
    options: [
      { value: 1, label: 'Новая' },
      { value: 2, label: 'В работе' },
      { value: 3, label: 'На проверке' },
      { value: 4, label: 'Завершена' },
      { value: 5, label: 'Отменена' },
    ]
  },
  {
    key: 'priorityId',
    label: 'Приоритет',
    options: [
      { value: 1, label: 'Низкий' },
      { value: 2, label: 'Средний' },
      { value: 3, label: 'Высокий' },
      { value: 4, label: 'Критичный' },
    ]
  },
  // { key: 'authorId', label: 'Автор', type: 'number', sortable: true, filterable: false },
  { key: 'authorName', label: 'Автор', type: 'text', sortable: true, filterable: true, readonly: true },
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

onMounted(() => {
  currentTaskName.value = props.task.name
  fetchComments()
  console.log(props.task)
})
</script>