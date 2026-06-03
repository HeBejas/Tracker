<template>
  <th :class="{ 'is-sortable': sortable }" @click="sortable && $emit('sort')">
    <div class="th-inner">
      <span class="label">{{ label }}</span>

      <span v-if="sortable" class="sort-icon" :class="sortState">
        {{ sortState === 'desc' ? '▼' : '▲' }}
      </span>
    </div>
  </th>
</template>

<script setup lang="ts">
defineProps<{
  label: string
  sortable?: boolean
  sortState?: 'asc' | 'desc' | 'none'
}>()

// Теперь мы просто кричим наверх "Меня кликнули!", без всяких ключей
defineEmits(['sort'])
</script>

<style scoped>
th {
  padding: 12px 8px;
  border-bottom: 2px solid #e2e8f0;
  color: #64748b;
  font-weight: 600;
  text-align: left;
}

.is-sortable {
  cursor: pointer;
  user-select: none; /* Запрещаем выделение текста при быстрых кликах */
}

.is-sortable:hover {
  color: #0f172a;
  background-color: #f8fafc;
}

.th-inner {
  display: flex;
  align-items: center;
  /* white-space: nowrap запретит тексту переноситься на новую строку и ломать высоту */
  white-space: nowrap;
}

/* ФИКС ТАНЦУЮЩЕЙ ТАБЛИЦЫ */
.sort-icon {
  display: inline-block;
  width: 16px; /* Жестко фиксируем ширину иконки */
  text-align: center;
  margin-left: 4px;
  font-size: 0.75rem;

  /* Иконка прозрачная по умолчанию, но место занимает! */
  opacity: 0;
  transition: opacity 0.2s;
}

/* При наведении показываем блеклую подсказку */
.is-sortable:hover .sort-icon {
  opacity: 0.3;
}

/* Если сортировка активна - показываем синим цветом */
.sort-icon.asc, .sort-icon.desc {
  opacity: 1;
  color: #2563eb;
}
</style>