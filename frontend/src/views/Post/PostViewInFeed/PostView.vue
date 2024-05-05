<script setup>
import {inject} from "vue";

const props = defineProps({
  post: Object,

})

// url to load images
const imgUrl = inject('imgUrl');

</script>

<template>
  <div class="bg-gray-50 rounded-lg shadow-lg m-1 ml-10 mr-10 p-4">
    <!-- Zona del usuario autor del post -->
    <div class="flex items-center mb-4">
      <!-- Verificamos si existe post.author y post.author.author.profileImage -->
      <img v-if="post.author && post.author.author && post.author.author.profileImage"
           :src="imgUrl + post.author.author.profileImage"
           :alt="post.author.author.fullName"
           class="w-12 h-12 rounded-full mr-4">
      <!-- Si no existe, puedes manejar un caso alternativo -->
<!--      <img v-else src="ruta/a/imagen/por/defecto.jpg" alt="Imagen de perfil por defecto">-->

      <div>
        <p class="font-bold">{{post.author.author.fullName}}</p>
        <p class="text-gray-700">{{post.author.author.bio}}</p>
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