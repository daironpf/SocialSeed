<script setup>
import {RouterView} from "vue-router";
import HeaderView from "@/views/HeaderView.vue";

</script>

<template lang="html">
  <main>
    <HeaderView />
    <div id="contenido">
      <RouterView />
    </div>
  </main>
</template>



<style scoped>
  #contenido {
    margin-top: 90px;
    margin-left: 30px;
  }
</style>