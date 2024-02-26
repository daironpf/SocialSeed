<script setup>
import { ref } from 'vue'
const greeting = ref('Hello World!')
</script>

<template>
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

  <p class="greeting">{{ greeting }}</p>

</template>

<style>
.greeting {
  color: red;
  font-weight: bold;
}
</style>