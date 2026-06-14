<template>
  <div class="db-table-container">
    <div class="table-controls">
      <input
          v-model="searchQuery"
          type="text"
          placeholder="Поиск по всем полям..."
          class="form-input"
      />
      <div class="actions-slot">
        <slot name="action"></slot>
      </div>
    </div>

    <div v-if="isLoading" class="loading-state">
      Загрузка данных...
    </div>

    <div v-else>
      <table class="tracker-table">
        <thead>
        <tr>
          <TableHeaderCell
              v-for="col in columns"
              :key="col.key"
              :label="col.label"
              :sortable="true"
              :sort-state="sortKey === col.key ? sortOrder : 'none'"
              @sort="sortBy(col.key)"
          />
          <TableHeaderCell
              v-if="mode !== 'Read'"
              label="Действия"
          />
        </tr>
        </thead>

        <tbody>
        <tr v-for="row in paginatedData" :key="row.id">
          <td v-for="col in columns" :key="col.key">
            {{ row[col.key] }}
          </td>

          <td v-if="mode !== 'Read'" class="actions-cell">
            <button
                v-if="mode === 'Inspect' || mode === 'CRUD'"
                @click="$emit('inspect', row)"
                class="icon-btn"
            >
              👁 Просмотр
            </button>

            <button
                v-if="mode === 'CRUD'"
                @click="$emit('edit', row)"
                class="icon-btn"
            >
              ✎ Изменить
            </button>

            <button
                v-if="mode === 'CRUD'"
                @click="$emit('delete', row)"
                class="icon-btn danger"
            >
              ✖ Удалить
            </button>
          </td>
        </tr>
        <tr v-if="paginatedData.length === 0">
          <td :colspan="columns.length + (mode !== 'Read' ? 1 : 0)" class="empty-state">
            Ничего не найдено
          </td>
        </tr>
        </tbody>
      </table>

      <div class="pagination" v-if="totalPages > 1">
        <button :disabled="currentPage === 1" @click="currentPage--">Назад</button>
        <span>Страница {{ currentPage }} из {{ totalPages }}</span>
        <button :disabled="currentPage === totalPages" @click="currentPage++">Вперед</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import TableHeaderCell from './TableHeaderCell.vue'
export interface TableColumn {
  key: string
  label: string
}

const props = defineProps<{
  columns: TableColumn[]
  data: any[]
  mode: 'Read' | 'Inspect' | 'CRUD'
  isLoading: boolean
}>()

defineEmits(['inspect', 'edit', 'delete'])

const searchQuery = ref('')
const sortKey = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')

const currentPage = ref(1)
const itemsPerPage = 5

const filteredData = computed(() => {
  if (!searchQuery.value) return props.data
  const query = searchQuery.value.toLowerCase()
  return props.data.filter(item =>
      Object.values(item).some(val =>
          String(val).toLowerCase().includes(query)
      )
  )
})

const sortedData = computed(() => {
  const dataCopy = [...filteredData.value]
  if (!sortKey.value) return dataCopy

  return dataCopy.sort((a, b) => {
    const valA = a[sortKey.value]
    const valB = b[sortKey.value]

    if (valA < valB) return sortOrder.value === 'asc' ? -1 : 1
    if (valA > valB) return sortOrder.value === 'asc' ? 1 : -1
    return 0
  })
})

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  const end = start + itemsPerPage
  return sortedData.value.slice(start, end)
})

const totalPages = computed(() => {
  return Math.ceil(sortedData.value.length / itemsPerPage) || 1
})

const sortBy = (key: string) => {
  if (sortKey.value === key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortKey.value = key
    sortOrder.value = 'asc'
  }
}
</script>

<style scoped>
.db-table-container { display: flex; flex-direction: column; gap: 16px; }
.table-controls { display: flex; justify-content: space-between; }
.form-input { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 6px; width: 300px; }
.tracker-table { width: 100%; border-collapse: collapse; text-align: left; font-size: 0.9rem; }
.tracker-table th { padding: 12px 8px; border-bottom: 2px solid #e2e8f0; color: #64748b; }
.tracker-table th.sortable { cursor: pointer; user-select: none; }
.tracker-table th.sortable:hover { color: #0f172a; }
.tracker-table td { padding: 12px 8px; border-bottom: 1px solid #f1f5f9; color: #1e293b; }
.actions-cell { display: flex; gap: 8px; }
.icon-btn { background: none; border: none; color: #64748b; cursor: pointer; border-radius: 4px; padding: 4px; }
.icon-btn:hover { background: #f1f5f9; color: #0f172a; }
.icon-btn.danger:hover { color: #dc2626; background: #fee2e2; }
.pagination { display: flex; gap: 16px; align-items: center; justify-content: center; margin-top: 16px; }
.loading-state, .empty-state { text-align: center; padding: 32px; color: #64748b; }
</style>