<template>
  <div class="admin-layout">
    <router-view />
  </div>
</template>

<script setup>
</script>

<style scoped>
.admin-layout {
  display: flex;
  flex-direction: column;
  gap: 20px;
  /* Не ставим высоту на 100vh, компонент займет столько места, сколько даст родительский фрейм */
}
.admin-wrapper {
  background: #ffffff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  border: 1px solid #e2e8f0;
}
/* Навигация внутри модуля (Табы) */
.module-tabs {
  display: flex;
  gap: 30px;
  border-bottom: 2px solid #e2e8f0;
  padding-bottom: 0;
}

.tab-btn {
  background: none;
  border: none;
  padding: 10px 4px;
  font-size: 1rem;
  font-weight: 500;
  color: #64748b;
  cursor: pointer;
  position: relative;
  transition: color 0.2s;
}

.tab-btn:hover { color: #0f172a; }

.tab-btn.active { color: #2563eb; font-weight: 600; }
.tab-btn.active::after {
  content: '';
  position: absolute;
  bottom: -2px; /* Накладываем поверх бордера родителя */
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #2563eb;
}

/* Контейнер контента */
.module-content {
  background: #ffffff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  border: 1px solid #e2e8f0;
}

/* Тулбар над таблицами */
.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.search-input { width: 300px; padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 6px; }
.search-input:focus { border-color: #2563eb; outline: none; }

/* Универсальные кнопки */
.action-btn { padding: 8px 16px; border-radius: 6px; font-weight: 500; cursor: pointer; border: none; }
.action-btn.primary { background: #2563eb; color: white; }
.action-btn.primary:hover { background: #1d4ed8; }
.action-btn.secondary { background: #f1f5f9; color: #475569; border: 1px solid #cbd5e1; }
.action-btn.secondary:hover { background: #e2e8f0; }

.icon-btn { background: none; border: none; color: #64748b; cursor: pointer; font-size: 0.85rem; padding: 4px 8px; border-radius: 4px; }
.icon-btn:hover { background: #f1f5f9; color: #0f172a; }

/* Таблицы (наследуют общий стиль или задаются здесь) */
.tracker-table { width: 100%; border-collapse: collapse; text-align: left; font-size: 0.9rem; }
.tracker-table th { color: #64748b; font-weight: 500; padding: 12px 8px; border-bottom: 1px solid #e2e8f0; }
.tracker-table td { padding: 12px 8px; border-bottom: 1px solid #f1f5f9; color: #1e293b; }
.fw-500 { font-weight: 500; }
.text-right { text-align: right; }

/* Бейджи */
.badge { padding: 4px 10px; border-radius: 12px; font-size: 0.75rem; font-weight: 600; }
.badge.active { background: #dcfce7; color: #166534; }
.badge.paused { background: #fef3c7; color: #92400e; }
.badge.role-admin { background: #fee2e2; color: #991b1b; }
.badge.role-user { background: #e0e7ff; color: #3730a3; }

/* Карточки в справочниках */
.settings-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
.settings-card { padding: 20px; border: 1px solid #e2e8f0; border-radius: 8px; }
.settings-card h3 { margin: 0 0 8px 0; color: #0f172a; font-size: 1.1rem; }
.settings-card p { margin: 0; color: #64748b; font-size: 0.9rem; line-height: 1.4; }
.mt-2 { margin-top: 16px; }
</style>

```

<!--    -->
<!--    -->
<!--    <div class="module-content card">-->
<!--      <div v-if="currentTab === 'projects'" class="tab-pane">-->
<!--        <div class="toolbar">-->
<!--          <input type="text" placeholder="Поиск проектов..." class="search-input form-input" />-->
<!--          <button class="action-btn primary">+ Создать проект</button>-->
<!--        </div>-->

<!--        <table class="tracker-table">-->
<!--          <thead>-->
<!--          <tr>-->
<!--            <th>ID</th>-->
<!--            <th>Название проекта</th>-->
<!--            <th>Руководитель</th>-->
<!--            <th>Статус</th>-->
<!--            <th></th>-->
<!--          </tr>-->
<!--          </thead>-->
<!--          <tbody>-->
<!--          <tr>-->
<!--            <td>1</td>-->
<!--            <td class="fw-500">Система управления задачами (Tracker)</td>-->
<!--            <td>Александров Б. К.</td>-->
<!--            <td><span class="badge active">Активен</span></td>-->
<!--            <td class="text-right"><button class="icon-btn">⚙️ Настроить</button></td>-->
<!--          </tr>-->
<!--          <tr>-->
<!--            <td>2</td>-->
<!--            <td class="fw-500">База данных ИжГТУ</td>-->
<!--            <td>Иванов А. А.</td>-->
<!--            <td><span class="badge paused">На паузе</span></td>-->
<!--            <td class="text-right"><button class="icon-btn">⚙️ Настроить</button></td>-->
<!--          </tr>-->
<!--          </tbody>-->
<!--        </table>-->
<!--      </div>-->

<!--      <div v-if="currentTab === 'users'" class="tab-pane">-->
<!--        <div class="toolbar">-->
<!--          <input type="text" placeholder="Поиск по фио или email..." class="search-input form-input" />-->
<!--          <button class="action-btn primary">+ Добавить пользователя</button>-->
<!--        </div>-->

<!--        <table class="tracker-table">-->
<!--          <thead>-->
<!--          <tr>-->
<!--            <th>Сотрудник</th>-->
<!--            <th>Email</th>-->
<!--            <th>Роль в системе</th>-->
<!--            <th></th>-->
<!--          </tr>-->
<!--          </thead>-->
<!--          <tbody>-->
<!--          <tr>-->
<!--            <td class="fw-500">Александров Б. К.</td>-->
<!--            <td>bogdan@tracker.local</td>-->
<!--            <td><span class="badge role-admin">Администратор</span></td>-->
<!--            <td class="text-right"><button class="icon-btn">Редактировать</button></td>-->
<!--          </tr>-->
<!--          <tr>-->
<!--            <td class="fw-500">Старыгин А. В.</td>-->
<!--            <td>starygin@tracker.local</td>-->
<!--            <td><span class="badge role-user">Пользователь</span></td>-->
<!--            <td class="text-right"><button class="icon-btn">Редактировать</button></td>-->
<!--          </tr>-->
<!--          </tbody>-->
<!--        </table>-->
<!--      </div>-->

<!--      <div v-if="currentTab === 'settings'" class="tab-pane">-->
<!--        <div class="settings-grid">-->
<!--          <div class="settings-card">-->
<!--            <h3>Статусы задач</h3>-->
<!--            <p>Настройка workflow и этапов выполнения.</p>-->
<!--            <button class="action-btn secondary mt-2">Управление</button>-->
<!--          </div>-->
<!--          <div class="settings-card">-->
<!--            <h3>Роли и доступы</h3>-->
<!--            <p>Матрица прав доступа к модулям системы.</p>-->
<!--            <button class="action-btn secondary mt-2">Управление</button>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->

<!--    </div>-->