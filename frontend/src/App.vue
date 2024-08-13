<script setup lang="ts">
import {computed} from "vue";
import {RouterView, useRouter} from "vue-router";
import HeaderView from "@/components/HeaderView.vue";
import "@/core/assets/main.css";

const router = useRouter();
const showHeader = computed<boolean>(() => {
  const routeMeta = router.currentRoute.value.meta as Record<string, unknown> | undefined;

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