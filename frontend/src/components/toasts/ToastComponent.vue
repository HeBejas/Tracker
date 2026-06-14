<template>
  <transition name="toast">
    <div
        v-if="visible"
        :class="['toast-notification', type, { 'is-paused': isPaused }]"
        :style="{ '--duration': duration + 'ms' }"
        @mouseenter="pauseTimer"
        @mouseleave="resumeTimer"
    >
      <div class="toast-header">
        <span class="toast-icon">
          <i v-if="type === 'success'" class="fas fa-check-circle"></i>
          <i v-if="type === 'error'" class="fas fa-times-circle"></i>
          <i v-if="type === 'warning'" class="fas fa-exclamation-triangle"></i>
        </span>
        <button class="toast-close" @click="hide">×</button>
      </div>

      <div class="toast-body">
        <span class="toast-message">{{ message }}</span>
      </div>

      <div class="toast-progress-bar"></div>
    </div>
  </transition>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  id: { type: Number, required: true },
  message: { type: String, required: true },
  type: { type: String, default: 'success' },
  duration: { type: Number, default: 3000 },
})

const emit = defineEmits(['remove'])

const visible = ref(true)
const isPaused = ref(false)

let timeoutId = null
let startTime = 0
let remainingTime = props.duration

const hide = () => {
  visible.value = false
  setTimeout(() => {
    emit('remove', props.id)
  }, 400)
}

const startTimer = () => {
  if (remainingTime <= 0) return
  startTime = Date.now()
  timeoutId = setTimeout(hide, remainingTime)
}

const pauseTimer = () => {
  if (props.duration <= 0) return
  isPaused.value = true
  clearTimeout(timeoutId)
  remainingTime -= Date.now() - startTime
}

const resumeTimer = () => {
  if (props.duration <= 0) return
  isPaused.value = false
  startTimer()
}

onMounted(() => {
  startTimer()
})

onUnmounted(() => {
  clearTimeout(timeoutId)
})
</script>

