<template>
  <FrameWrapperComponent>
    <FrameHeaderComponent>
      Сотрудники
    </FrameHeaderComponent>

    <DataBaseTable
        :columns="userColumns"
        :data="users"
        mode="CRUD"
        :isLoading="isLoading"
        @inspect="onInspect"
        @edit="onEdit"
        @delete="onDelete"
    >
      <template #action>
        <CreateButton @click="showCreateModal = true">
          Пригласить сотрудника
        </CreateButton>
      </template>
    </DataBaseTable>

  </FrameWrapperComponent>

  <InspectModal
      :show="showInspectModal"
      :title="`Просмотр профиля «${selectedUser?.fullName}»`"
      :data="selectedUser"
      :fields="userColumns"
      @close="showInspectModal = false"
  />

  <CreateModal
      :show="showCreateModal"
      title="Добавить сотрудника"
      :fields="userFormFields"
      submitLabel="Добавить"
      @close="showCreateModal = false"
      @submit="onCreate"
  />

  <DeleteModal
      :show="showDeleteModal"
      title="Удаление сотрудника"
      :text="`Вы уверены, что хотите убрать пользователя «${selectedUser?.fullName}» из воркспейса?`"
      @close="showDeleteModal = false"
      @confirm="onDeleteConfirm"
  />

  <EditModal
      :show="showEditModal"
      :title="`Изменить данные «${selectedUser?.fullName}»`"
      :fields="userFormFields"
      :data="selectedUser"
      @close="showEditModal = false"
      @submit="onUpdate"
  />
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'
import { useToast } from '@/utils/ToastUtils'

import FrameWrapperComponent from '@/components/frame/FrameWrapperComponent.vue'
import FrameHeaderComponent from '@/components/frame/FrameHeaderComponent.vue'
import DataBaseTable from '@/components/tables/DataBaseTable.vue'
import CreateButton from '@/components/buttons/CreateBtn.vue'

import CreateModal from '@/components/modals/CreateModal.vue'
import DeleteModal from '@/components/modals/DeleteModal.vue'
import InspectModal from '@/components/modals/InspectModal.vue'
import EditModal from '@/components/modals/EditModal.vue'

const route = useRoute()
const authStore = useAuthStore()
const toast = useToast()

const users = ref<any[]>([])
const selectedUser = ref<any>(null)
const projectOptions = ref<any[]>([])
const isLoading = ref(true)

// Управление состоянием модальных окон
const showCreateModal = ref(false)
const showDeleteModal = ref(false)
const showInspectModal = ref(false)
const showEditModal = ref(false)

const userColumns: any = [
  { key: 'id', label: 'ID', sortable: true },
  { key: 'fullName', label: 'ФИО', sortable: true, filterable: true },
  { key: 'email', label: 'Email', filterable: true },
  { key: 'role', label: 'Роль', filterable: true },
  { key: 'project', label: 'Закрепленный проект', filterable: true }
]

const userFormFields: any = computed(() => [
  { key: 'fullName', label: 'ФИО сотрудника', type: 'text', placeholder: 'Иванов Иван' },
  { key: 'email', label: 'Электронная почта', type: 'text', placeholder: 'example@tracker.com' },
  {
    key: 'role',
    label: 'Тип доступа (Роль)',
    type: 'select',
    options: [
      { value: 2, label: 'Менеджер' },
      { value: 3, label: 'Сотрудник' }
    ]
  }
  // ,
  // {
  //   key: 'projectId',
  //   label: 'Закрепить за проектом (для Сотрудника)',
  //   type: 'select',
  //   options: projectOptions.value
  // }
])

const transformUser = (user: any) => ({
  ...user,
  role: user.role?.name || '—',
  project: user.project?.name || 'Все проекты'
})

const fetchEmployees = async () => {
  const workspaceId = route.params.id
  if (!workspaceId) return

  try {
    isLoading.value = true

    const usersResponse = await axios.get(`/api/workspaces/${workspaceId}/users`)
    const userData = usersResponse.data
    users.value = Array.isArray(userData) ? userData.map(transformUser) : [transformUser(userData)]

  } catch (error) {
    console.error(`Ошибка при загрузке данных: ${error}`)
    toast.error('Не удалось загрузить список сотрудников')
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchEmployees()
})

// Обработчики таблицы
const onInspect = (user: any) => {
  selectedUser.value = user
  showInspectModal.value = true
}

const onEdit = (user: any) => {
  selectedUser.value = user
  showEditModal.value = true
}

const onDelete = (user: any) => {
  selectedUser.value = user
  showDeleteModal.value = true
}

// CRUD операции
const onCreate = async (formData: any) => {
  const workspaceId = route.params.id

  try {
    let response
    const basePayload = {
      email: formData.email,
      fullName: formData.fullName,
      workspaceId: Number(workspaceId),
      inviterId: authStore.userId
    }

    if (formData.roleId === 2) {
      response = await axios.post('/api/auth/invite/manager', basePayload)
    } else {
      // if (!formData.projectId) {
      //   toast.error('Для роли "Сотрудник" необходимо выбрать проект!')
      //   return
      // }
      response = await axios.post('/api/auth/invite/employee', {
        ...basePayload,
        projectId: formData.projectId
      })
    }

    // Оставляем обычный alert, так как тост слишком быстро исчезнет
    // и пользователь не успеет скопировать временный пароль
    alert(`Пользователь успешно добавлен!\n\nEmail: ${response.data.email}\nВременный пароль: ${response.data.rawPassword}`)

    showCreateModal.value = false
    await fetchEmployees()
  } catch (error: any) {
    console.error(`Ошибка при создании инвайта: ${error}`)
    toast.error(error.response?.data || 'Не удалось пригласить сотрудника')
  }
}

const onDeleteConfirm = async () => {
  if (!selectedUser.value) return

  try {
    await axios.delete(`/api/users/${selectedUser.value.id}`)
    users.value = users.value.filter(u => u.id !== selectedUser.value.id)
    showDeleteModal.value = false
    toast.success('Сотрудник успешно удален')
  } catch (error) {
    console.error(`Ошибка при удалении: ${error}`)
    toast.error('Не удалось удалить сотрудника')
  }
}

const onUpdate = async (data: any) => {
  if (!selectedUser.value) return

  try {
    const response = await axios.put(`/api/users/${selectedUser.value.id}`, data)
    const index = users.value.findIndex(u => u.id === selectedUser.value.id)
    if (index !== -1) {
      users.value[index] = transformUser(response.data)
    }
    showEditModal.value = false
    toast.success('Данные сотрудника обновлены')
  } catch (error) {
    console.error(`Ошибка при обновлении: ${error}`)
    toast.error('Не удалось обновить профиль')
  }
}
</script>