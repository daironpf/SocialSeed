<script setup>
import {computed} from "vue";
import {RouterView, useRouter} from "vue-router";
import HeaderView from "@/components/HeaderView.vue";
import "@/core/assets/main.css";

const router = useRouter();
const showHeader = computed(() => {
  const routeMeta = router.currentRoute.value.meta;

  return Boolean(routeMeta?.showHeader);
});
</script>

<template lang="html">
  <main>
    <HeaderView v-if="showHeader"/>
    <div>
      <RouterView :key="$route.fullPath"/>
    </div>
  </main>
</template>


<style scoped>

</style>