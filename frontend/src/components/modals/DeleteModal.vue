<!-- DeleteModal.vue -->
<template>
  <ModalOverlay :show="show" :title="title" @close="$emit('close')">
      <div class="modal-content">
        <p class="modal-text">{{ text }}</p>
      </div>
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
  text: string
}>()

defineEmits<{
  close: []
  confirm: []
}>()
</script>

