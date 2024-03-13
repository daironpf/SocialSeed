<script setup>
import {RouterView} from "vue-router";
import HeaderView from "@/views/HeaderView.vue";

let api_url = "http://127.0.0.1:8081/api/v0.0.1/"
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