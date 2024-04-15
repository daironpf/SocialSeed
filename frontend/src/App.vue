<script setup>
import {RouterView, useRouter} from "vue-router";
import HeaderView from "@/views/HeaderView.vue";
import {computed} from "vue";

const router = useRouter();
const showHeader = computed(() => {
  const routeMeta = router.currentRoute.value.meta;
  return routeMeta ? routeMeta.showHeader !== false : true;
});
</script>

<template lang="html">
  <main>
    <HeaderView v-if="showHeader" />
    <div>
      <RouterView :key="$route.fullPath"/>
    </div>
  </main>
</template>



<style scoped>

</style>