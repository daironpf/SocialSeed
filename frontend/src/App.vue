<template lang="html">
  <div id="encabezado" class="w-full bg-blue-500 text-white py-4 fixed top-0 z-50">
    <div class="container mx-auto flex justify-between items-center">
      <div class="logo text-2xl font-bold">SocialSeed</div>
      <nav class="menu">
        <ul class="flex space-x-4">
          <li><a href="#" class="hover:text-blue-300"><fa icon="fa-solid fa-home" /></a></li>
          <li><a href="#" class="hover:text-blue-300"><fa icon="fa-solid fa-user" /></a></li>
          <li><a href="#" class="hover:text-blue-300"><fa icon="fa-solid fa-bell" /></a></li>
          <li><a href="#" class="hover:text-blue-300"><fa icon="fa-solid fa-envelope" /></a></li>
          <li><a href="#" class="hover:text-blue-300"><fa icon="fa-solid fa-cog" /></a></li>
        </ul>
      </nav>
    </div>
  </div>

  <div class="container mx-auto mt-24 flex">
    <div class="w-1/4 px-4">
      <!-- Sección de Sugerencias de Amistad -->
      <div class="bg-white p-4 rounded-lg shadow">
        <h2 class="text-lg font-semibold mb-4">Sugerencias de Amistad</h2>
        <!-- Contenido de las sugerencias de amistad -->
      </div>
    </div>
    <div class="w-1/2 px-4">
      <!-- Sección de Feed -->
      <div class="bg-white p-4 rounded-lg shadow">
        <h2 class="text-lg font-semibold mb-4">Feed</h2>
        <!-- Contenido del feed -->
      </div>
    </div>
    <div class="w-1/4 px-4">
      <!-- Sección de Chat y Sugerencias de Seguidores -->
      <div class="bg-white p-4 rounded-lg shadow">
        <h2 class="text-lg font-semibold mb-4">Chat y Sugerencias de Seguidores</h2>
        <!-- Contenido del chat y sugerencias de seguidores -->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

</script>