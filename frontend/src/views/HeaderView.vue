<script setup>
import {RouterLink} from "vue-router";
</script>

<template>
  <div id="encabezado" class="w-full bg-blue-500 text-white py-4 fixed top-0 z-50">
    <div class="container mx-auto flex justify-between items-center">
      <RouterLink to="/">
        <div class="logo text-2xl font-bold">SocialSeed</div>
      </RouterLink>

      <nav class="menu">
        <ul class="flex space-x-4">
          <li><RouterLink to="/" class="hover:text-blue-300"><fa icon="fa-solid fa-home" /></RouterLink></li>
          <li><RouterLink to="/su-profile/me" class="hover:text-blue-300"><fa icon="fa-solid fa-user" /></RouterLink></li>
          <li><RouterLink to="/post/8754" class="hover:text-blue-300"><fa icon="fa-solid fa-bell" /></RouterLink></li>
          <li><RouterLink to="" class="hover:text-blue-300"><fa icon="fa-solid fa-envelope" /></RouterLink></li>
          <li><RouterLink to="" class="hover:text-blue-300"><fa icon="fa-solid fa-cog" /></RouterLink></li>
        </ul>
      </nav>
    </div>
  </div>
</template>

<style scoped>

</style>