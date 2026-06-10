<template>
  <div class="frame-nav-header">
    <NavActiveBtn
        v-for="link in resolvedLinks"
        :key="link.path"
        :text="link.label"
        :isActive="$route.path === link.path"
        @click="navigate(link.path)"
    />
  </div>
</template>


<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import NavActiveBtn from '../buttons/NavActiveBtn.vue'

const router = useRouter()
const route = useRoute()

const resolvedLinks = computed(() => {
  const parentRoute = route.matched.find(r => r.meta?.showChildTabs)

  if (!parentRoute || !parentRoute.children) return []

  return parentRoute.children
      .filter(child => child.meta?.breadcrumb && typeof child.meta.breadcrumb === 'string')
      .map(child => {
        const linkRecord = router.resolve({
          name: child.name,
          params: route.params
        })

        return {
          path: linkRecord.fullPath,
          label: child.meta.breadcrumb as string
        }
      })
})

const navigate = (path: string) => {
  router.push(path)
}
</script>

<style scoped>
.frame-nav-header {
  display: flex;
  gap: 30px;
  padding-bottom: 0;
  border-bottom: 2px solid #e2e8f0;
  margin-bottom: 24px;
}
</style>
