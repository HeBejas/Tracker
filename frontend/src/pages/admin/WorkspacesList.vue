<template>
  <AdminNavHeaderComponent :links="adminLinks" />
  <AdminWrapperComponent>

    <AdminHeaderComponent title="Список рабочих сред">
      <button class="action-btn primary" @click="showCreateModal = true">Создать рабочую среду</button>
    </AdminHeaderComponent>

    <DataBaseTable
        :columns="workspaceColumns"
        :data="workspaces"
        mode="CRUD"
        :isLoading="isLoading"
        @inspect="goToWorkspace"
        @edit="onEdit"
        @delete="onDelete"
    />

    <CreateModal
        :show="showCreateModal"
        title="Создание рабочей среды"
        :fields="workspaceFormFields"
        submitLabel="Создать"
        @close="showCreateModal = false"
        @submit="onCreate"
    />

    <DeleteModal
        :show="showDeleteModal"
        title="Удаление рабочей среды"
        :text="`Вы уверены, что хотите удалить рабочую среду ${selectedWorkspace?.id}?`"
        @close="showDeleteModal = false"
        @confirm="onDeleteConfirm"
    />

    <EditModal
        :show="showEditModal"
        :title="`Изменить рабочую среду ${selectedWorkspace?.id} `"
        :fields="workspaceFormFields"
        :data="selectedWorkspace"
        @close="showEditModal = false"
        @submit="onUpdate"
    />

  </AdminWrapperComponent>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import DataBaseTable from '../../components/tables/DataBaseTable.vue'
import type { TableColumn } from '../../components/tables/DataBaseTable.vue'
import axios from 'axios'

import AdminNavHeaderComponent from '../../components/admin/AdminNavHeaderComponent.vue'
import AdminWrapperComponent from '../../components/admin/AdminWrapperComponent.vue'
import AdminHeaderComponent from '../../components/admin/AdminHeaderComponent.vue'
import CreateModal from "../../components/modals/CreateModal.vue";
import DeleteModal from "../../components/modals/DeleteModal.vue";
import EditModal from "../../components/modals/EditModal.vue";

const adminLinks = [
  '/admin/workspaces',
  '/admin/users',
  '/admin/tariffs'
]

const router = useRouter()
const isLoading = ref(true)

const workspaces = ref<any[]>([])
const selectedWorkspace = ref<any>(null)

const tariffs = ref<any[]>([])

const transformWorkspace = (workspace: any ) => ({
  ...workspace,
  tariff: workspace.tariff?.name || '—',
  status: workspace.status?.name || '—',
  createdAt: workspace.createdAt ? new Date(workspace.createdAt).toLocaleDateString('ru-RU') : '—'
})

const workspaceColumns: any = [
  { key: 'id', label: 'ID' },
  { key: 'tariff', label: 'Тариф' },
  { key: 'status', label: 'Статус' },
  { key: 'createdAt', label: 'Создан' }
]

const workspaceFormFields: any = computed(() => [
  { key: 'tariff', label: 'Тариф', type: 'select',
    options: tariffs.value.map(t => ({ value: t.name, label: t.name }))
  },
  { key: 'status', label: 'Статус', type: 'select',
    options: [
      { value: 'active', label: 'Активен' },
      { value: 'suspended', label: 'Приостановлен' },
      { value: 'suspended_nonpayment', label: 'Приостановлен за неуплату' }
    ]
  }
])


const goToWorkspace = (workspace: any) => {
  router.push(`/admin/workspaces/${workspace.id}`)
}

const fetchWorkspaces = async () => {
  try {
    isLoading.value = true
    const response = await axios.get('/api/workspaces')
    workspaces.value = response.data.map(transformWorkspace)
  } catch (error) {
    console.error('Ошибка при загрузке списка:', error)
  } finally {
    isLoading.value = false
  }
}

const fetchTariffs = async () => {
  try {
    const response = await axios.get('/api/tariffs')
    tariffs.value = response.data
  } catch (error) {
    console.error('Ошибка при загрузке тарифов:', error)
  }
}

onMounted(() => {
  fetchWorkspaces()
  fetchTariffs()
})

const showCreateModal = ref(false)
const showDeleteModal = ref(false)
const showEditModal = ref(false)

const onCreate = async (data: any) => {
  const response = await axios.post('/api/workspaces', data)
  workspaces.value.push(transformWorkspace(response.data))
  showCreateModal.value = false
}

const onDelete = async (workspace: any) => {
  showDeleteModal.value = true
  selectedWorkspace.value = workspace
}

const onDeleteConfirm = async () => {
  try {
    await axios.delete(`/api/workspaces/${selectedWorkspace.value.id}`)
    workspaces.value = workspaces.value.filter(w => w.id !== selectedWorkspace.value.id)
    showDeleteModal.value = false
  } catch (error) {
    alert(`Ошибка при удалении: ${error}`)
  }
}

const onEdit = (workspace: any) => {
  selectedWorkspace.value = workspace
  showEditModal.value = true
}

const onUpdate = async (data: any) => {
  try {
    const response = await axios.put(`/api/workspaces/${selectedWorkspace.value.id}`, data)
    const index = workspaces.value.findIndex(w => w.id === selectedWorkspace.value.id)
    if (index !== -1) {
      workspaces.value[index] = transformWorkspace(response.data)
    }
    showEditModal.value = false
  } catch (error) {
    console.error('Ошибка при обновлении:', error)
  }
}

</script>