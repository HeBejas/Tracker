
<template>
  <div class="table-container">
    <table class="data-table">
      <thead>
      <tr>
        <th
            v-for="col in columns"
            :key="col.key"
            :class="{ 'sortable': col.sortable !== false }"
            @click="col.sortable !== false ? emit('update-sort', col.key) : null"
        >
          <div class="th-content">
            {{ col.label }}
            <span v-if="col.sortable !== false" class="sort-icon">
                <template v-if="sortKey === col.key">
                  {{ sortOrder === 'asc' ? '▲' : '▼' }}
                </template>
                <template v-else>↕</template>
              </span>
          </div>
        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="row in data" :key="row.id" @click="emit('row-click', row)">
        <td v-for="col in columns" :key="col.key">

          <div v-if="col.type === 'progress-bar' && col.from && col.to" class="progress-wrapper">
            <span class="progress-text">{{ row[col.to] }} / {{ row[col.from] }}</span>
            <progress :value="row[col.to]" :max="row[col.from]"></progress>
          </div>

          <template v-else>
            {{ renderCellValue(row, col) }}
          </template>

        </td>
      </tr>

      <tr v-if="data.length === 0">
        <td :colspan="columns.length" class="empty-state">
          Ничего не найдено
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
interface ColumnOption {
  value: any
  label: string
}

interface Column {
  key: string
  label: string
  type?: string
  from?: string
  to?: string
  sortable?: boolean
  options?: ColumnOption[]
}

const props = defineProps<{
  columns: Column[]
  data: any[]
  sortKey: string
  sortOrder: 'asc' | 'desc'
}>()

const emit = defineEmits<{
  (e: 'row-click', row: any): void
  (e: 'update-sort', key: string): void
}>()

const renderCellValue = (row: any, col: Column) => {
  const rawValue = row[col.key]
  if (col.type === 'date') {
    return formatDate(rawValue)
  }
  if (col.options) {
    const foundOption = col.options.find(opt => opt.value === rawValue)
    return foundOption ? foundOption.label : rawValue
  }
  return rawValue ?? '—'
}

const formatDate = (dateString) => {
  if (!dateString) return '—'
  return new Intl.DateTimeFormat('ru-RU', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit'
  }).format(new Date(dateString))
}
</script>

<style scoped>
.table-container {
  width: 100%;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.data-table {
  width: 100%;
  min-width: 750px;
  border-collapse: collapse;
  text-align: left;
  font-size: 14px;
}

.data-table th {
  padding: 12px 16px;
  background-color: #f9fafb;
  border-bottom: 1px solid #e5e7eb;
  color: #374151;
  font-weight: 600;
  user-select: none;
  font-size: 1rem;
}

.data-table th.sortable {
  cursor: pointer;
}

.data-table th.sortable:hover {
  background-color: #f3f4f6;
}

.th-content {
  display: flex;
  align-items: center;
  gap: 6px;
}

.sort-icon {
  font-size: 10px;
  color: #9ca3af;
}

.data-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #e5e7eb;
}

.data-table tbody tr {
  cursor: pointer;
  transition: background-color 0.15s ease;
}

.data-table tbody tr:hover {
  background-color: #f3f6f9;
}

.progress-wrapper {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 5px;
  width: 100%;
  max-width: 130px;
}

.progress-text {
  font-size: 12px;
  min-width: 40px;
}

progress {
  width: 100%;
  max-width: 120px;
  height: 8px;
  border-radius: 4px;
}
progress::-webkit-progress-bar {
  background-color: #e5e7eb;
  border-radius: 4px;
}
progress::-webkit-progress-value {
  background-color: #3b82f6;
  border-radius: 4px;
}

.empty-state {
  text-align: center;
  padding: 32px !important;
  color: #9ca3af;
}
</style>