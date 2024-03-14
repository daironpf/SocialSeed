<script setup>
import {RouterView, useRouter} from "vue-router";
import HeaderView from "@/views/HeaderView.vue";
import {computed} from "vue";

let api_url = "http://127.0.0.1:8081/api/v0.0.1/";

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
      <RouterView />
    </div>
  </main>
</template>



<style scoped>

</style>