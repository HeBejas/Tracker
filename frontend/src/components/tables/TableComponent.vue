<script setup lang="ts" generic="T extends Record<string, unknown>">
import { ref, computed } from 'vue'
import TableHeaderComponent from './TableHeaderComponent.vue'
import TableBodyComponent from './TableBodyComponent.vue'

export interface ColumnOption {
  value: string | number
  label: string
}

export interface TableColumn {
  key: string
  label: string
  type?: 'text' | 'progress-bar' | 'date' | 'number'
  from?: string
  to?: string
  sortable?: boolean
  filterable?: boolean
  name_field?: boolean
  options?: ColumnOption[]
}

const props = defineProps<{
  columns: TableColumn[]
  data: T[]
}>()

const emit = defineEmits<{
  (e: 'row-click', row: T): void
}>()

const searchQuery = ref('')
const sortKey = ref<string>('name')
const sortOrder = ref<'asc' | 'desc'>('asc')
const activeFilters = ref<Record<string, any>>({})

const nameColumn = computed(() => props.columns.find(c => c.name_field))

const handleSortUpdate = (key: string) => {
  if (sortKey.value === key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortKey.value = key
    sortOrder.value = 'asc'
  }
}

const handleFilterUpdate = (key: string, value: any) => {
  if (value === null) {
    delete activeFilters.value[key]
  } else {
    activeFilters.value[key] = value
  }
}

const handleRemoveFilter = (key: string) => {
  delete activeFilters.value[key]
}

const processedData = computed(() => {
  let result = [...props.data]

  // Поиск по названию
  if (searchQuery.value && nameColumn.value) {
    const query = searchQuery.value.toLowerCase()
    const searchKey = nameColumn.value.key
    result = result.filter(item =>
        String(item[searchKey] || '').toLowerCase().includes(query)
    )
  }

  // Применение фильтров
  Object.keys(activeFilters.value).forEach(key => {
    const filterValue = activeFilters.value[key]
    const col = props.columns.find(c => c.key === key)

    if (!filterValue || !col) return

    result = result.filter(item => {
      const itemValue = item[key]

      if (col.options) {
        if (Array.isArray(filterValue) && filterValue.length > 0) {
          return filterValue.includes(itemValue)
        }
        return true
      }

      if (col.type === 'date') {
        if (!filterValue.date) return true

        const itemDateObj = new Date(itemValue as string)

        const iYear = itemDateObj.getFullYear()
        const iMonth = String(itemDateObj.getMonth() + 1).padStart(2, '0')
        const iDay = String(itemDateObj.getDate()).padStart(2, '0')
        const itemDateString = `${iYear}-${iMonth}-${iDay}`

        const filterDateString = filterValue.date

        if (filterValue.mode === 'from') return itemDateString >= filterDateString
        if (filterValue.mode === 'to') return itemDateString <= filterDateString
        return itemDateString === filterDateString
      }

      // Обработка числового фильтра
      if (col.type === 'number') {
        if (filterValue.value === undefined || filterValue.value === '') return true
        const iNum = Number(itemValue)
        const fNum = Number(filterValue.value)

        if (filterValue.mode === 'from') return iNum >= fNum
        if (filterValue.mode === 'to') return iNum <= fNum
        return iNum === fNum
      }

      if (typeof filterValue === 'string') {
        return String(itemValue || '').toLowerCase().includes(filterValue.toLowerCase())
      }

      return true
    })
  })

  // Сортировка
  result.sort((a, b) => {
    const valA = a[sortKey.value]
    const valB = b[sortKey.value]

    if (valA === undefined || valA === null) return 1
    if (valB === undefined || valB === null) return -1

    if (typeof valA === 'string') {
      return sortOrder.value === 'asc'
          ? valA.localeCompare(valB as string)
          : (valB as string).localeCompare(valA)
    }

    return sortOrder.value === 'asc'
        ? (valA > valB ? 1 : -1)
        : (valA < valB ? 1 : -1)
  })

  return result
})
</script>

<template>
  <div class="table-wrapper">
    <TableHeaderComponent
        :columns="columns"
        :active-filters="activeFilters"
        :search-query="searchQuery"
        @update:search="searchQuery = $event"
        @update-filter="handleFilterUpdate"
        @remove-filter="handleRemoveFilter"
    >
      <template #action v-if="$slots.action">
        <slot name="action"></slot>
      </template>
    </TableHeaderComponent>

    <TableBodyComponent
        :columns="columns"
        :data="processedData"
        :sort-key="sortKey"
        :sort-order="sortOrder"
        @update-sort="handleSortUpdate"
        @row-click="emit('row-click', $event)"
    />
  </div>
</template>

<style scoped>
.table-wrapper {
  display: grid;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  border-radius: 8px;
}
</style>