<template>
  <div v-if="show" class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-window">

      <div class="modal-header">
        <h3>{{ title }}</h3>
        <button class="close-btn" @click="$emit('close')">✖</button>
      </div>

      <div class="modal-body">
        <slot />
      </div>

      <div class="modal-footer">
        <slot name="footer" />
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  show: boolean
  title: string
}>()

defineEmits<{
  close: []
}>()

</script>

<style scoped>
.modal-overlay {
  position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
  background: rgba(15, 23, 42, 0.6); backdrop-filter: blur(2px);
  display: flex; justify-content: center; align-items: center; z-index: 1000;
}

.modal-window {
  background: white; padding: 24px; border-radius: 12px; width: 450px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1); animation: modal-pop 0.2s ease-out;
}

@keyframes modal-pop { from { opacity: 0; transform: scale(0.95); } to { opacity: 1; transform: scale(1); } }


.modal-header {
  display: flex; justify-content: space-between; align-items: center;
  border-bottom: 1px solid #e2e8f0; padding-bottom: 12px; margin-bottom: 20px;
}

.modal-header h3 { margin: 0; color: #0f172a; }

.close-btn {
  background: none; border: none; font-size: 1.2rem; cursor: pointer; color: #64748b;
}
.close-btn:hover { color: #0f172a; }

.modal-body {
  display: flex; flex-direction: column; gap: 16px;
}



.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  border-top: 1px solid #f1f5f9; /* Едва заметная линия отсечения футера */
  padding-top: 16px;
  margin-top: 24px;
}

.action-btn {
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
}

.action-btn.secondary {
  background: #ffffff;
  color: #334155;
  border: 1px solid #cbd5e1;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05); /* Легкая тень для объема */
}

.action-btn.secondary:hover {
  background: #f8fafc;
  color: #0f172a;
  border-color: #94a3b8;
}

.action-btn.secondary:active {
  background: #f1f5f9;
}
</style>
