<template>
  <button class="regular-object-btn" type="button">
    <span v-if="$slots.icon" class="btn-icon">
      <slot name="icon" />
    </span>
    <span class="btn-text">
      <slot />
    </span>
  </button>
</template>

<style scoped>
.regular-object-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  font-family: inherit;
  font-size: 14px;
  font-weight: 500;
  line-height: 1.2;
  background: #ffffff;
  color: #334155;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.2s ease;
  width: fit-content;
}

.regular-object-btn:hover {
  background: #f8fafc;
  color: #0f172a;
  border-color: #94a3b8;
}

.regular-object-btn:active {
  transform: translateY(1px);
  box-shadow: none;
}

.btn-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
}

.btn-icon :deep(svg) {
  width: 100%;
  height: 100%;
  stroke: currentColor;
  stroke-width: 1.5;
  fill: none;
}
</style>