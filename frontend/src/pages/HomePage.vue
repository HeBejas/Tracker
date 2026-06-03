<template>
  <h1 style="padding-left: 20px">Домашняя страница</h1>
  <div class="kanban-dashboard">
    <div class="kanban-column">
      <div class="column-header">

        <div class="header-title">
          <h3>Открыт</h3>
          <span class="task-count">1</span>
        </div>
        <div class="header-actions">
          <button class="icon-btn">+</button>
          <button class="icon-btn">...</button>
        </div>
      </div>

      <div class="task-card">
        <div class="card-top">
          <div class="task-id">
            <span class="user-icon">👤</span> TRACKER-26
          </div>
          <div class="card-actions">
            <span class="action-icon">=</span>
            <span class="action-icon">📄</span>
          </div>
        </div>
        <div class="card-title">Разработать новый макет</div>
        <div class="card-timer timer-new">
          <span class="spin-icon">◌</span> Новый: 25мин 19с
        </div>
      </div>

      <button class="add-task-btn">+ Добавить задачу</button>
    </div>

    <div class="kanban-column">
      <div class="column-header">
        <div class="header-title">
          <h3>В работе</h3>
          <span class="task-count">3</span>
        </div>
        <div class="header-actions">
          <button class="icon-btn">...</button>
        </div>
      </div>

      <div class="task-card">
        <div class="card-top">
          <div class="task-id">
            <span class="user-icon">👤</span> TRACKER-12
          </div>
          <div class="card-actions">
            <span class="action-icon">=</span>
            <span class="action-icon">📄</span>
          </div>
        </div>
        <div class="card-title">Продуктовая проработка</div>
        <div class="card-timer timer-active">
          <span class="play-icon">▶</span> В работе: 6мес 1нед
        </div>
      </div>

      <div class="task-card">
        <div class="card-top">
          <div class="task-id">
            <span class="user-icon">👤</span> TRACKER-21
          </div>
          <div class="card-actions">
            <span class="action-icon">=</span>
            <span class="action-icon">📄</span>
          </div>
        </div>
        <div class="card-title">Интерактив</div>
        <div class="card-timer timer-active">
          <span class="play-icon">▶</span> В работе: 5мес 2нед
        </div>
      </div>

      <div class="task-card">
        <div class="card-top">
          <div class="task-id">
            <span class="user-icon">👤</span> TRACKER-7
          </div>
          <div class="card-actions">
            <span class="action-icon">=</span>
            <span class="action-icon">📄</span>
          </div>
        </div>
        <div class="card-title">Разработать новый макет</div>
        <div class="card-timer timer-active">
          <span class="play-icon">▶</span> В работе: 6мес 1нед
        </div>
      </div>
    </div>

    <div class="kanban-column">
      <div class="column-header">
        <div class="header-title">
          <h3>Закрыт</h3>
        </div>
        <div class="header-actions">
          <button class="icon-btn">...</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Заглушка дашборда. В будущем сюда прикрутишь Axios для стягивания задач из PostgreSQL через твой Spring Boot бэкенд.
</script>

<style scoped>
.kanban-dashboard {
  display: flex;
  gap: 12px;
  padding: 20px;
  background-color: #ffffff;
  min-height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
  align-items: flex-start;
}

.kanban-column {
  background-color: #f4f5f7;
  border-radius: 8px;
  width: 320px;
  min-width: 320px;
  display: flex;
  flex-direction: column;
  padding: 12px;
  gap: 10px;
}

.column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4px 4px 8px 4px;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-title h3 {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
  color: #172b4d;
}

.task-count {
  font-size: 13px;
  color: #6b778c;
}

.header-actions {
  display: flex;
  gap: 4px;
}

.icon-btn {
  background: none;
  border: none;
  color: #6b778c;
  cursor: pointer;
  font-size: 16px;
  padding: 0 4px;
  border-radius: 3px;
}

.icon-btn:hover {
  background-color: #ebecf0;
}

.task-card {
  background-color: #ffffff;
  border-radius: 6px;
  padding: 12px;
  box-shadow: 0 1px 2px rgba(9, 30, 66, 0.1);
  display: flex;
  flex-direction: column;
  gap: 10px;
  cursor: pointer;
  border: 1px solid transparent;
}

.task-card:hover {
  border-color: #dfe1e6;
}

.card-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.task-id {
  font-size: 11px;
  color: #5e6c84;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.user-icon {
  font-size: 12px;
  opacity: 0.7;
}

.card-actions {
  display: flex;
  gap: 6px;
  color: #6b778c;
  font-size: 12px;
}

.action-icon {
  opacity: 0.6;
}

.card-title {
  font-size: 13px;
  font-weight: 500;
  color: #172b4d;
  line-height: 1.4;
}

.card-timer {
  font-size: 11px;
  font-weight: 500;
  padding: 4px 8px;
  border-radius: 12px;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  align-self: flex-start;
}

.timer-new {
  background-color: #f4f5f7;
  color: #5e6c84;
}

.timer-active {
  background-color: #e6fcff;
  color: #006644;
}

.timer-active .play-icon {
  color: #0052cc;
  font-size: 10px;
}

.add-task-btn {
  background: none;
  border: none;
  color: #5e6c84;
  font-size: 13px;
  text-align: left;
  padding: 8px;
  cursor: pointer;
  border-radius: 4px;
  margin-top: 4px;
}

.add-task-btn:hover {
  background-color: #ebecf0;
  color: #172b4d;
}
</style>