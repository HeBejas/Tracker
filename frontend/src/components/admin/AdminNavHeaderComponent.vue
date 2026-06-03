<template>
  <div class="admin-nav-header">
    <NavActiveBtn
      v-for="link in resolvedLinks"
      :key="link.path"
      :text="link.label"
      :isActive="$route.path.includes(link.path)"
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

  const props = defineProps<{
    links: string[]
  }>()

  const resolvedLinks = computed(() =>{
    return props.links.map(link => {
      const linkRecord = router.resolve(link)
      return {
        path: link,
        label: linkRecord.meta?.breadcrumb || link
      }
    })
  })

  const navigate = (path: string) => {
    router.push(path)
  }
</script>

<style scoped>
  .admin-nav-header{
    display: flex;
    gap: 30px;
    padding-bottom: 0;

    border-bottom: 2px solid #e2e8f0;
  }
</style>
