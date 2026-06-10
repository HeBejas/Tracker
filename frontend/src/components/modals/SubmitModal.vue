<template>
  <ModalOverlay :show="show" :title="title" @close="$emit('close')">

    <div class="modal-content">
      <p class="modal-text">{{ text }}</p>
    </div>

    <template #footer>
      <ModalSecondaryBtn @click="$emit('close')">Отмена</ModalSecondaryBtn>
      <ModalPrimaryBtn @click="onSubmit">{{ submitLabel }}</ModalPrimaryBtn>
    </template>

  </ModalOverlay>
</template>

<script setup lang="ts">
import ModalOverlay from "./ModalOverlay.vue"
import ModalPrimaryBtn from "./modals_buttons/ModalPrimaryBtn.vue"
import ModalSecondaryBtn from "./modals_buttons/ModalSecondaryBtn.vue"

const props = defineProps({
  show: { type: Boolean, required: true },
  title: { type: String, required: true },
  text: { type: String, required: true },
  submitLabel: { type: String, default: 'Подтвердить' }
})

const emit = defineEmits<{
  close: []
  submit: []
}>()

const onSubmit = () => {
  emit('submit')
}
</script>

<style scoped>

.modal-text {
  color: #475569;
  font-size: 1rem;
  margin: 0;
}
</style>