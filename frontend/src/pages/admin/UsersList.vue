<template>
  <AdminNavHeaderComponent :links="adminLinks" />
  <AdminWrapperComponent>

    <AdminHeaderComponent title="Список пользователей">
      <button class="action-btn primary" @click="showCreateModal = true">Добавить пользователя</button>
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
        title="Просмотр профиля сотрудника"
        :fields="userColumns" @close="showInspectModal = false"
        :data="selectedUser"
    />
    <CreateModal
        :show="showCreateModal"
        title="Добавить пользователя"
        :fields="userFormFields"
        submitLabel="Создать"
        @close="showCreateModal = false"
        @submit="onCreate"
    />

    <DeleteModal
        :show="showDeleteModal"
        title="Удаление пользователя"
        :text="`Вы уверены, что хотите удалить пользователя «${selectedUser?.fullName}»?`"
        @close="showDeleteModal = false"
        @confirm="onDeleteConfirm"
    />

    <EditModal
        :show="showEditModal"
        :title="`Изменить «${selectedUser?.fullName}»`"
        :fields="userFormFields"
        :data="selectedUser"
        @close="showEditModal = false"
        @submit="onUpdate"
    />

  </AdminWrapperComponent>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import DataBaseTable from '../../components/tables/DataBaseTable.vue'
import type { TableColumn } from '../../components/tables/DataBaseTable.vue'
import InspectModal from '../../components/modals/InspectModal.vue'

import AdminNavHeaderComponent from '../../components/admin/AdminNavHeaderComponent.vue'
import AdminWrapperComponent from '../../components/admin/AdminWrapperComponent.vue'
import AdminHeaderComponent from "../../components/admin/AdminHeaderComponent.vue";
import DeleteModal from "../../components/modals/DeleteModal.vue";
import CreateModal from "../../components/modals/CreateModal.vue";
import EditModal from "../../components/modals/EditModal.vue";

const adminLinks = [
  '/admin/workspaces',
  '/admin/users',
  '/admin/tariffs'
]

const transformUser = (user: any) => ({
  ...user,
  role: user.role?.name || '—',
  status: user.status?.name || '—'
})

// 2. Состояние страницы
const users = ref<any[]>([])
const selectedUser = ref<any>(null)

const isLoading = ref(true)

// 3. Настраиваем колонки. 'key' должен строго совпадать с названиями полей в Java
const userColumns: TableColumn[] = [
  { key: 'id', label: 'ID' },
  { key: 'fullName', label: 'ФИО сотрудника' },
  { key: 'email', label: 'Email' },
  { key: 'role', label: 'Роль' },
  { key: 'status', label: 'Статус' }
]

// 4. Метод загрузки данных с твоего контроллера
const fetchUsers = async () => {
  try {
    isLoading.value = true
    const response = await axios.get('/api/users')
    users.value = response.data.map(transformUser)
  } catch (error) {
    console.error('Ошибка при загрузке списка пользователей:', error)
  } finally {
    isLoading.value = false
  }
}

const showCreateModal = ref(false)
const showDeleteModal = ref(false)
const showInspectModal = ref(false)
const showEditModal = ref(false)

const onInspect = (user: any) => {
  selectedUser.value = user
  showInspectModal.value = true
}

const onEdit = (user: any) => {
  selectedUser.value = user
  showEditModal.value = true
}

const onCreate = async (data: any) => {
  console.log('Создаём пользователя:', data)
  // const response = await axios.post('/api/tariffs', data)
  // tariffs.value.push(transformTariff(response.data))
  showCreateModal.value = false
}

const onDelete = async (user: any) => {
  showDeleteModal.value = true
  selectedUser.value = user
}

const onDeleteConfirm = async () => {
  try {
    // await axios.delete(`/api/tariffs/${selectedTariff.value.id}`)
    // tariffs.value = tariffs.value.filter(t => t.id !== selectedTariff.value.id)
    showDeleteModal.value = false
  } catch (error) {
    alert(`Ошибка при удалении: ${error}`)
  }
}

const onUpdate = async (data: any) => {
  try {
    // const response = await axios.put(`/api/tariffs/${selectedTariff.value.id}`, data)
    // const index = tariffs.value.findIndex(t => t.id === selectedTariff.value.id)
    // if (index !== -1) {
    //   tariffs.value[index] = transformTariff(response.data)
    // }
    showEditModal.value = false
  } catch (error) {
    console.error('Ошибка при обновлении:', error)
  }
}

onMounted(() => {
  fetchUsers()
})

const userFormFields: any = [
  { key: 'email', label: 'Email', type: 'text', placeholder: 'Введите email' },
  { key: 'fullName', label: 'ФИО', type: 'text', placeholder: 'Введите полное имя' },
  // { key: 'workspaceId', label: 'ID Пространства', type: 'number' },
  {
    key: 'role',
    label: 'Роль',
    type: 'select',
    options: [
      { value: 'admin', label: 'Администратор' },
      { value: 'manager', label: 'Менеджер' },
      { value: 'employee', label: 'Сотрудник' }
    ]
  },
  {
    key: 'status',
    label: 'Статус',
    type: 'select',
    options: [
      { value: 'active', label: 'Активен' },
      { value: 'disabled', label: 'Заблокирован' }
    ]
  }
]
</script>
