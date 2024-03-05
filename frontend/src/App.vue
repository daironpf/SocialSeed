<script setup>
import {RouterView} from "vue-router";
import HeaderView from "@/views/HeaderView.vue";

</script>

<template lang="html">
  <main>
    <HeaderView />
    <div>
      <RouterView />
    </div>
  </main>
</template>



<style scoped>

</style>