<template>
    <AdminHeaderComponent title="Список сотрудников">
      <button class="action-btn primary" @click="showCreateModal = true">Пригласить сотрудника</button>
    </AdminHeaderComponent>

    <DataBaseTable
        :columns="userColumns"
        :data="users"
        mode="CRUD"
        :isLoading="isLoading"
        @inspect="onInspect"
        @edit="onEdit"
        @delete="onDelete"
    />

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
        :message="`Вы уверены, что хотите убрать пользователя «${selectedUser?.fullName}» из воркспейса?`"
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
import axios from 'axios'
import { useAuthStore } from '../../../stores/auth'

import DataBaseTable from "../../../components/tables/DataBaseTable.vue";
import AdminNavHeaderComponent from "../../../components/admin/AdminNavHeaderComponent.vue";
import AdminHeaderComponent from "../../../components/admin/AdminHeaderComponent.vue";
import AdminWrapperComponent from "../../../components/admin/AdminWrapperComponent.vue";

import CreateModal from "../../../components/modals/CreateModal.vue";
import DeleteModal from "../../../components/modals/DeleteModal.vue"
import InspectModal from "../../../components/modals/InspectModal.vue";
import EditModal from "../../../components/modals/EditModal.vue";

const authStore = useAuthStore()
const adminLinks = [
  '/admin/workspaces',
  '/admin/users',
  '/admin/tariffs'
]

const props = defineProps<{
  workspaceId: number
}>()

const users = ref<any[]>([])
const selectedUser = ref<any>(null)

const projectOptions = ref<any[]>([])
const isLoading = ref(true)


const userColumns: any = [
  { key: 'id', label: 'ID' },
  { key: 'fullName', label: 'ФИО' },
  { key: 'email', label: 'Email' },
  { key: 'role', label: 'Роль' },
  { key: 'project', label: 'Закрепленный проект' }
]

const transformUser = (user: any ) => ({
  ...user,
  role: user.role?.name || '—',
  project: user.project?.name || 'Все проекты'
})

const fetchEmployees = async () => {
  try {
    isLoading.value = true

    const usersResponse = await axios.get(`/api/workspaces/${props.workspaceId}/users`)
    const userData = usersResponse.data

    users.value = Array.isArray(userData)
        ? userData.map(transformUser)
        : [transformUser(userData)]

    const projectsResponse = await axios.get(`/api/workspaces/${props.workspaceId}/projects`)
    projectOptions.value = projectsResponse.data.map((p: any) => ({
      value: p.id,
      label: p.name
    }))
  } catch (error) {
    console.error('Ошибка при загрузке данных:', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchEmployees()
})

const showCreateModal = ref(false)
const showDeleteModal = ref(false)
const showInspectModal = ref(false)
const showEditModal = ref(false)

const userFormFields: any = computed(() => [
  { key: 'fullName', label: 'ФИО сотрудника', type: 'text', placeholder: 'Иванов Иван' },
  { key: 'email', label: 'Электронная почта', type: 'text', placeholder: 'example@tracker.com' },
  {
    key: 'roleId',
    label: 'Тип доступа (Роль)',
    type: 'select',
    options: [
      { value: 2, label: 'Менеджер' },
      { value: 3, label: 'Сотрудник' }
    ]
  }
])

const onInspect = async (user: any ) => {
  selectedUser.value = user
  showInspectModal.value = true
}

const onEdit = (user: any ) => {
  selectedUser.value = user
  showEditModal.value = true
}

const onDelete = async (user: any ) => {
  selectedUser.value = user
  showDeleteModal.value = true
}

const onCreate = async (formData: any ) => {
  console.log(props.workspaceId)
  try {
    let response
    const basePayload = {
      email: formData.email,
      fullName: formData.fullName,
      workspaceId: props.workspaceId,
      // inviterId: authStore.user?.id || 1 // Тот, кто создает
      inviterId: 4
    }

    if (formData.roleId === 2) {
      response = await axios.post('/api/auth/invite/manager', basePayload)
    } else {
      // Проверяем, выбрал ли админ проект для работяги
      // if (!formData.projectId) {
      //   alert('Ошибка: Для роли "Сотрудник" обязательно нужно выбрать проект!')
      //   return
      // }
      // Стучимся на ручку Сотрудника
      response = await axios.post('/api/auth/invite/employee', {
        ...basePayload,
        projectId: formData.projectId
      })
    }

    alert(`Пользователь успешно добавлен!\n\nEmail: ${response.data.email}\nВременный пароль: ${response.data.rawPassword}`)

    showCreateModal.value = false
    await fetchEmployees() // Перезагружаем список
  } catch (error: any) {
    alert(error.response?.data || 'Ошибка при создании инвайта')
  }
}

const onDeleteConfirm = async () => {
  try {
    await axios.delete(`/api/users/${selectedUser.value.id}`)
    users.value = users.value.filter(u => u.id !== selectedUser.value.id)
    showDeleteModal.value = false
  } catch (error) {
    alert('Ошибка при удалении сотрудника')
  }
}

const onUpdate = async (data: any ) => {
  try {
    const response = await axios.put(`/api/users/${selectedUser.value.id}`, data)
    const index = users.value.findIndex(u => u.id === selectedUser.value.id)
    if (index !== -1) {
      users.value[index] = transformUser(response.data)
    }
    showEditModal.value = false
  } catch (error) {
    console.error('Ошибка при обновлении профиля:', error)
  }
}
</script>