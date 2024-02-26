<template lang="html">
  <main></main>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

// Puedes acceder a la ruta actual a través de la variable `route`
console.log('Ruta actual:', route);
</script>

<style scoped>

</style>