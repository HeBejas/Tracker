<template>
  <div class="home-page">
    <h1>Домашняя страница</h1>
    <p>Добро пожаловать в рабочее пространство.</p>

    <div class="widgets-container">
      <div class="widget card">
        <h3>Мои задачи</h3>
        <p class="big-number">5</p>
        <span class="status">В работе</span>
      </div>

      <div class="widget card">
        <h3>Ближайший дедлайн</h3>
        <p>Завтра, 18:00</p>
        <span class="status danger">Критично</span>
      </div>
    </div>
  </div>
</template>

<script setup>
// Здесь будут запросы к бэкенду для получения сводки по задачам
</script>

<style scoped>
.home-page {
  /* Не нужно задавать ширину/высоту 100vh, этот блок растянется сам в content-area */
  display: flex;
  flex-direction: column;
  gap: 20px;
}

h1 {
  margin: 0;
  color: #2c3e50;
}

.widgets-container {
  display: flex;
  gap: 20px;
}

.widget.card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  flex: 1;
  border: 1px solid #eaeaea;
}

.big-number {
  font-size: 32px;
  font-weight: bold;
  color: #3498db;
  margin: 10px 0;
}

.status {
  font-size: 14px;
  color: #7f8c8d;
}

.status.danger {
  color: #e74c3c;
  font-weight: bold;
}
</style>