<!-- DeleteModal.vue -->
<template>
  <ModalOverlay :show="show" :title="title" @close="$emit('close')">

      <p class="delete-message">{{ message }}</p>

      <template #footer>
        <ModalSecondaryBtn @click="$emit('close')">Отмена</ModalSecondaryBtn>
        <ModalDeleteBtn @click="$emit('confirm')">Удалить</ModalDeleteBtn>
      </template>

  </ModalOverlay>
</template>

<script setup lang="ts">
import ModalOverlay from './ModalOverlay.vue'
import ModalSecondaryBtn from "./modals_buttons/ModalSecondaryBtn.vue";
import ModalDeleteBtn from "./modals_buttons/ModalDeleteBtn.vue";

defineProps<{
  show: boolean
  title: string
  message: string
}>()

defineEmits<{
  close: []
  confirm: []
}>()
</script>

<style scoped>
.delete-content {
  display: flex; flex-direction: column; gap: 16px;
}

.delete-message {
  color: #475569; font-size: 0.95rem; margin: 0;
}
</style>