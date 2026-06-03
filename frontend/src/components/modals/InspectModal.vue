<template>
  <ModalOverlay :show="show" :title="title" @close="$emit('close')">

    <div class="inspect-container" v-if="data">
      <div class="inspect-grid">
        <div
            class="inspect-card"
            v-for="field in fields"
            :key="field.key"
        >
          <span class="card-label">{{ field.label }}</span>
          <div class="card-value">
            <template v-if="data[field.key] !== null && data[field.key] !== ''">
              {{ data[field.key] }}
            </template>
            <span v-else class="empty-dash">—</span>
          </div>
        </div>
      </div>
    </div>

    <template #footer>
      <ModalSecondaryBtn @click="$emit('close')">Закрыть</ModalSecondaryBtn>
    </template>

  </ModalOverlay>
</template>

<script setup lang="ts">
import ModalOverlay from "./ModalOverlay.vue";
import ModalSecondaryBtn from "./modals_buttons/ModalSecondaryBtn.vue";

interface InspectField {
  key: string
  label: string
}

defineProps<{
  show: boolean
  title: string
  data: any
  fields: InspectField[]
}>()

defineEmits(['close'])
</script>

<style scoped>
/* Контейнер для полей */
.inspect-container {
  padding: 4px 0;
}

/* Современная сетка: на средних экранах бьется на 2 колонки, на маленьких — в одну */
.inspect-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 16px;
}

/* Карточка отдельного поля */
.inspect-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: #f8fafc; /* Очень мягкий slate фон */
  border: 1px solid #e2e8f0; /* Тонкая аккуратная рамка */
  padding: 12px 16px;
  border-radius: 8px; /* Скругление посовременнее */
  transition: all 0.2s ease;
}

/* Эффект при наведении на карточку параметра */
.inspect-card:hover {
  background: #f1f5f9;
  border-color: #cbd5e1;
}

/* Лейбл: мелкий, строгий, уходит на второй план */
.card-label {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #64748b; /* Серый slate цвет */
  margin-bottom: 6px;
  font-weight: 600;
}

/* Само значение: крупное, контрастное, легко читается */
.card-value {
  font-size: 15px;
  color: #0f172a; /* Глубокий темный цвет */
  font-weight: 500;
  word-break: break-word;
  line-height: 1.4;
}

/* Стили для пустого значения */
.empty-dash {
  color: #94a3b8;
  font-weight: 400;
}

/* Кнопки и футер */

</style>