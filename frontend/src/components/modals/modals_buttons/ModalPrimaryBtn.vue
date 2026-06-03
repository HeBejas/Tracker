<template>
  <button class="modal-action-btn primary">
    <slot></slot>
  </button>
</template>

<style scoped>
  .modal-action-btn.primary {
    background: #2563eb;
    color: white;
  }

  .modal-action-btn.primary:hover {
    background: #1d4ed8;
  }

  .modal-action-btn:hover {
    background: #2563eb;
    box-shadow: 0 2px 4px rgba(59, 130, 246, 0.2);
  }
  .modal-action-btn:active {
    background: #1d4ed8;
    transform: translateY(1px);
  }
  .modal-action-btn:disabled {
    background: #cbd5e1 !important;
    color: #ffffff !important;
    cursor: not-allowed !important;
    box-shadow: none !important;
    transform: none !important;
  }
</style>