<!--<script setup lang="ts">-->
<!--</script>-->
<!--<template>-->
<!--  <div class="frame-header">-->
<!--    <h2>-->
<!--      <slot />-->
<!--    </h2>-->
<!--  </div>-->
<!--</template>-->

<!--<style scoped>-->
<!--.frame-header{-->
<!--  display: flex;-->
<!--  justify-content: space-between;-->
<!--  align-items: center;-->
<!--}-->
<!-- h2 {-->
<!--  margin: 0;-->
<!--  font-size: 1.5rem;-->
<!--  color: #0f172a;-->
<!--}-->
<!--</style>-->
<template>
  <div class="frame-header">
    <h2>
      <slot />
    </h2>
    <div class="header-actions" v-if="$slots.actions">
      <slot name="actions" />
    </div>
  </div>
</template>

<style scoped>
.frame-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
h2 {
  margin: 0;
  font-size: 2rem;
  color: #0f172a;
}
.header-actions {
  display: flex;
  gap: 10px;
}
</style>