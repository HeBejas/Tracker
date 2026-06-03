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
        :data="selectedUser"
        :fields="userColumns" @close="showInspectModal = false"
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
        :message="`Вы уверены, что хотите удалить пользователя «${selectedUser?.fullName}»?`"
        @close="showDeleteModal = false"
        @confirm="onDeleteConfirm"
    />

    <UpdateModal
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
import UpdateModal from "../../components/modals/UpdateModal.vue";

const adminLinks = [
  '/admin/workspaces',
  '/admin/users',
  '/admin/tariffs'
]

const transformUser = (user) => ({
  ...user,
  role: user.role?.name || '—',
  status: user.status?.name || '—'
})

// 2. Состояние страницы
const users = ref([])
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
    const response = await axios.get('http://localhost:8080/api/users')
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

const selectedUser = ref<User | null>(null)

const onInspect = (user) => {
  selectedUser.value = user
  showInspectModal.value = true
}

const onEdit = (user) => {
  selectedUser.value = user
  showEditModal.value = true
}

const onCreate = async (data) => {
  console.log('Создаём пользователя:', data)
  // const response = await axios.post('http://localhost:8080/api/tariffs', data)
  // tariffs.value.push(transformTariff(response.data))
  showCreateModal.value = false
}

const onDelete = async (tariff) => {
  showDeleteModal.value = true
  selectedUser.value = tariff
}

const onDeleteConfirm = async () => {
  try {
    // await axios.delete(`http://localhost:8080/api/tariffs/${selectedTariff.value.id}`)
    // tariffs.value = tariffs.value.filter(t => t.id !== selectedTariff.value.id)
    showDeleteModal.value = false
  } catch (error) {
    alert('Ошибка при удалении:', error)
  }
}

const onUpdate = async (data) => {
  try {
    // const response = await axios.put(`http://localhost:8080/api/tariffs/${selectedTariff.value.id}`, data)
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

const userFormFields = [
  { key: 'email', label: 'Email', type: 'text', placeholder: 'Введите email' },
  { key: 'fullName', label: 'ФИО', type: 'text', placeholder: 'Введите полное имя' },
  // { key: 'workspaceId', label: 'ID Пространства', type: 'number' },
  {
    key: 'role',
    label: 'Роль',
    type: 'select',
    options: [
      { value: 1, label: 'Администратор' },
      { value: 2, label: 'Менеджер' },
      { value: 3, label: 'Пользователь' }
    ]
  },
  {
    key: 'status',
    label: 'Статус',
    type: 'select',
    options: [
      { value: 1, label: 'Активен' },
      { value: 2, label: 'Заблокирован' }
    ]
  }
]
</script>
