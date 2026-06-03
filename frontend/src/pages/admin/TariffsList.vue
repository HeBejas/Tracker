<template>
  <AdminNavHeaderComponent :links="adminLinks" />
  <AdminWrapperComponent>

    <AdminHeaderComponent title="Список тарифов">
      <button class="action-btn primary" @click="showCreateModal = true">Создать тариф</button>
    </AdminHeaderComponent>

    <DataBaseTable
        :columns="tariffColumns"
        :data="tariffs"
        mode="CRUD"
        :isLoading="isLoading"
        @inspect="onInspect"
        @edit="onEdit"
        @delete="onDelete"
    />


    <InspectModal
        :show="showInspectModal"
        :title="`Просмотр тарифа «${selectedTariff?.name}»`"
        :data="selectedTariff"
        :fields="tariffColumns"
        @close="showInspectModal = false"
    />

    <CreateModal
        :show="showCreateModal"
        title="Создание тарифа"
        :fields="tariffFormFields"
        submitLabel="Создать"
        @close="showCreateModal = false"
        @submit="onCreate"
    />

    <DeleteModal
        :show="showDeleteModal"
        title="Удаление тарифа"
        :message="`Вы уверены, что хотите удалить тариф «${selectedTariff?.name}»?`"
        @close="showDeleteModal = false"
        @confirm="onDeleteConfirm"
    />

    <UpdateModal
        :show="showEditModal"
        :title="`Изменить «${selectedTariff?.name}»`"
        :fields="tariffFormFields"
        :data="selectedTariff"
        @close="showEditModal = false"
        @submit="onUpdate"
    />

  </AdminWrapperComponent>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

import DataBaseTable from "../../components/tables/DataBaseTable.vue";
import AdminNavHeaderComponent from "../../components/admin/AdminNavHeaderComponent.vue";
import AdminHeaderComponent from "../../components/admin/AdminHeaderComponent.vue";
import AdminWrapperComponent from "../../components/admin/AdminWrapperComponent.vue";

import CreateModal from "../../components/modals/CreateModal.vue";
import DeleteModal from "../../components/modals/DeleteModal.vue"
import InspectModal from "../../components/modals/InspectModal.vue";
import UpdateModal from "../../components/modals/UpdateModal.vue";

const adminLinks = [
  '/admin/workspaces',
  '/admin/users',
  '/admin/tariffs'
]

const tariffs = ref([])
const isLoading = ref(true)

const transformTariff = (tariff) => ({
  ...tariff,
  status: tariff.status?.name || '—'
})

const fetchTariffs = async () => {
  try {
    isLoading.value = true
    const response = await axios.get('http://localhost:8080/api/tariffs')
    tariffs.value = response.data.map(transformTariff) // response.data . Вызов transformTariff для каждого элемента
  } catch (error) {
    console.error('Ошибка при загрузке списка:', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchTariffs()
})

const tariffColumns = [
  { key: 'id', label: 'ID' },
  { key: 'name', label: 'Название' },
  { key: 'maxProjects', label: 'Лимит проектов' },
  { key: 'maxUsers', label: 'Лимит пользователей' },
  { key: 'cost', label: 'Стоимость' },
  { key: 'status', label: 'Статус' }
]

//Модальные окна
const showCreateModal = ref(false)
const showDeleteModal = ref(false)
const showInspectModal = ref(false)
const showEditModal = ref(false)

const selectedTariff = ref(null)

const tariffFormFields = [
  { key: 'name', label: 'Название', type: 'text', placeholder: 'Введите название' },
  { key: 'maxProjects', label: 'Лимит проектов', type: 'number' },
  { key: 'maxUsers', label: 'Лимит пользователей', type: 'number' },
  { key: 'cost', label: 'Стоимость', type: 'number' },
  {
    key: 'status',
    label: 'Статус',
    type: 'select',
    options: [
      { value: 1, label: 'Активный' },
      { value: 2, label: 'Архивный' }
    ]
  }
]

const onInspect= async (tariff) => {
  showInspectModal.value = true
  selectedTariff.value = tariff
}

const onCreate = async (data) => {
  console.log('Создаём тариф:', data)
  const response = await axios.post('http://localhost:8080/api/tariffs', data)
  tariffs.value.push(transformTariff(response.data))
  showCreateModal.value = false
}

const onDelete = async (tariff) => {
  showDeleteModal.value = true
  selectedTariff.value = tariff
}

const onEdit = (tariff) => {
  selectedTariff.value = tariff
  showEditModal.value = true
}

const onDeleteConfirm = async () => {
  try {
    await axios.delete(`http://localhost:8080/api/tariffs/${selectedTariff.value.id}`)
    tariffs.value = tariffs.value.filter(t => t.id !== selectedTariff.value.id)
    showDeleteModal.value = false
  } catch (error) {
    alert('Ошибка при удалении:', error)
  }
}

const onUpdate = async (data) => {
  try {
    const response = await axios.put(`http://localhost:8080/api/tariffs/${selectedTariff.value.id}`, data)
    // Обновляем в списке
    const index = tariffs.value.findIndex(t => t.id === selectedTariff.value.id)
    if (index !== -1) {
      tariffs.value[index] = transformTariff(response.data)
    }
    showEditModal.value = false
  } catch (error) {
    console.error('Ошибка при обновлении:', error)
  }
}
</script>
