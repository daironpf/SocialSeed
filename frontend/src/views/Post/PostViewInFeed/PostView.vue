<script setup>
const props = defineProps({
  post: Object
})

</script>

<template>
  <!--        publicacion N-->
  <div class="bg-gray-50 rounded-lg shadow-lg m-1 ml-10 mr-10 p-4">
    <!-- Zona del usuario autor del post -->
    <div class="flex items-center mb-4">
      <img src="/img/tesla.png" alt="Foto de perfil de Nikola Tesla" class="w-12 h-12 rounded-full mr-4">
      <div>
        <p class="font-bold">{{post.author.author.fullName}}</p>
        <p class="text-gray-700">Innovador incansable con una mente brillante y una visión revolucionaria para el futuro.</p>
      </div>
    </div>
    <div>
      <p class="text-lg text-gray-800 text-lg">
        {{post.content}}
      </p>
    </div>
    <!-- Zona de la botonera del post -->
    <!--      Botones para personalizar el Post-->
    <div class="max-w-lg mx-auto mt-3">
      <div class="flex flex-row">
        <div class="basis-1/12"></div>
        <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
          <fa icon="fa-solid fa-thumbs-up" class="text-gray-400"/>
          <p class="ml-2">Me Gusta</p>
        </div>
        <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
          <fa icon="fa-solid fa-comment" class="text-gray-400"/>
          <p class="ml-2">Comentar</p>
        </div>
        <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
          <fa icon="fa-solid fa-comment" class="text-gray-400"/>
          <p class="ml-2">Compartir</p>
        </div>
        <div class="basis-1/12"></div>
      </div>
    </div>
  </div>

</template>

<style scoped>

</style>