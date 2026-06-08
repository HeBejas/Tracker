<template>
  <ModalOverlay :show="show" :title="title" @close="$emit('close')">

    <form id="change-password-modal-form" @submit.prevent="onSubmit" class="modal-form">
      <div class="form-group">
        <label>Старый пароль</label>
        <input type="password" v-model="oldPassword" class="form-input" />
      </div>
      <div class="form-group">
        <label>Новый пароль</label>
        <input type="password" v-model="newPassword" class="form-input" />
      </div>
    </form>

    <template #footer>
      <ModalSecondaryBtn @click="$emit('close')">Отмена</ModalSecondaryBtn>
      <ModalPrimaryBtn type="submit" form="change-password-modal-form">Изменить</ModalPrimaryBtn>
    </template>

  </ModalOverlay>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import ModalSecondaryBtn from '../modals/modals_buttons/ModalSecondaryBtn.vue'
import ModalPrimaryBtn from '../modals/modals_buttons/ModalPrimaryBtn.vue'
import ModalOverlay from '../modals/ModalOverlay.vue'
const emit = defineEmits<{
  close: []
  submit: [payload: { oldPassword: string; newPassword: string }]
}>()

const oldPassword = ref('')
const newPassword = ref('')

const props = defineProps({
  show: { type: Boolean, required: true },
  title: { type: String, required: true }
})

const onSubmit = () => {
  emit('submit', {
    oldPassword: oldPassword.value,
    newPassword: newPassword.value
  })

  oldPassword.value = ''
  newPassword.value = ''
}
</script>
