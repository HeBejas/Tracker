<template>
  <header class="app-header">
    <div class="header-left">
      <HeaderNavItem to="/home" no-active class="icon-only">
        <template #icon>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
            <polyline points="9 22 9 12 15 12 15 22"></polyline>
          </svg>
        </template>
      </HeaderNavItem>

      <div class="vertical-divider"></div>

      <template v-for="(crumb, index) in breadcrumbs" :key="crumb.to">
        <HeaderNavItem :to="crumb.to">
          {{ crumb.label }}
        </HeaderNavItem>
        <span v-if="index < breadcrumbs.length - 1" class="slash">/</span>
      </template>
    </div>

    <div class="header-right">
<!--      <NavItem>-->
<!--        <template #icon>-->
<!--          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">-->
<!--            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>-->
<!--            <polyline points="7 10 12 15 17 10"></polyline>-->
<!--            <line x1="12" y1="15" x2="12" y2="3"></line>-->
<!--          </svg>-->
<!--        </template>-->
<!--        Выгрузить-->
<!--      </NavItem>-->

      <HeaderNavItem class="icon-only">
        <template #icon>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="1"></circle>
            <circle cx="19" cy="12" r="1"></circle>
            <circle cx="5" cy="12" r="1"></circle>
          </svg>
        </template>
      </HeaderNavItem>
    </div>
  </header>
</template>

<script setup>
  import { computed } from 'vue'
  import { useRoute } from 'vue-router'
  import HeaderNavItem from '../nav/HeaderNavItem.vue'

  const route = useRoute()
  const breadcrumbs = computed(() => {
    return route.matched
      .filter(item => item.meta && item.meta.breadcrumb)
      .map(item => {
        const label = typeof item.meta.breadcrumb === 'function'
            ? item.meta.breadcrumb(route)
            : item.meta.breadcrumb
        return {
          label: label,
          to: item.path
        }
      })
  })
</script>

<style scoped>
.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 16px;
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
  box-sizing: border-box;
}

.header-left, .header-right {
  display: flex;
  align-items: center;
  gap: 6px;
}

.icon-only {
  padding: 6px;
}

.vertical-divider {
  width: 1px;
  height: 20px;
  background-color: #e0e0e0;
  margin: 0 8px;
}

.slash {
  color: #a0a0a0;
  font-size: 14px;
  margin: 0 2px;
  user-select: none;
}
</style>