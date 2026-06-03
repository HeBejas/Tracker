<template>
  <button class="modal-action-btn secondary">
    <slot></slot>
  </button>
</template>

<style scoped>
  .modal-action-btn.secondary {
    background: #ffffff;
    color: #334155;
    border: 1px solid #cbd5e1;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  }
  .modal-action-btn.secondary:hover {
    background: #f8fafc;
    color: #0f172a;
    border-color: #94a3b8;
  }
</style>