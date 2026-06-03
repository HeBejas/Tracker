<template>
  <button class="modal-action-btn danger">
    <slot></slot>
  </button>
</template>

<style scoped>
.modal-action-btn.danger {
  background: #ef4444;
  color: white;
}

.modal-action-btn.danger:hover {
  background: #dc2626;
  box-shadow: 0 2px 4px rgba(239, 68, 68, 0.25);
}

.modal-action-btn.danger:active {
  background: #b91c1c;
  transform: translateY(1px);
}
</style>