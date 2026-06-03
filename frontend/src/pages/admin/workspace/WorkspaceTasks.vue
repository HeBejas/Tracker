<template>
  <div class="tasks-tab">
    <div class="toolbar">
      <h3>Задачи проекта</h3>
      <button class="action-btn primary">+ Добавить задачу</button>
    </div>

    <p class="text-muted">Здесь будет таблица с задачами для проекта ID: <strong>{{ projectId }}</strong></p>

  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
// Берем ID, чтобы знать, какие задачи просить у бэкенда
const projectId = route.params.id

// Позже добавишь:
// onMounted(() => fetchTasksByProjectId(projectId))
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.toolbar h3 {
  margin: 0;
  color: #0f172a;
}
.text-muted {
  color: #64748b;
}
.action-btn { padding: 8px 16px; border-radius: 6px; font-weight: 500; cursor: pointer; border: none; }
.action-btn.primary { background: #2563eb; color: white; }
.action-btn.primary:hover { background: #1d4ed8; }
</style>