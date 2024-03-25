<template>
  <div class="bg-gray-50 rounded-lg shadow m-1 ml-10 mr-10 p-2">
    <div class="max-w-lg mx-auto flex items-center border-b border-gray-300 pb-4 mb-4">
      <img :src="currentUser.profileImage" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
      <button @click="showDialog" class="flex-1 px-4 py-2 bg-gray-50 text-gray-600 rounded border border-gray-400 shadow-md hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
        Crear publicación
      </button>

      <div v-if="isDialogOpen"
           class="fixed left-0 top-0 bg-black bg-opacity-50 w-screen h-screen flex justify-center items-center"
           @click="hideDialog">
        <div class="bg-white rounded shadow-md w-[40%] h-[50%] flex flex-col" @click.stop>
          <!-- Encabezado -->
          <div class="flex items-center justify-between p-4 border-b border-gray-300">
            <div class="flex items-center">
              <img :src="currentUser.profileImage" alt="Foto de usuario" class="w-8 h-8 rounded-full mr-2">
              <span class="text-gray-700 font-semibold">{{ currentUser.fullName }}</span>
            </div>
            <button @click="hideDialog" class="text-gray-600 hover:text-gray-800 focus:outline-none">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          <!-- Contenido -->
          <div class="p-4 flex-grow">
            <textarea class="w-full h-full border border-gray-300 rounded-md p-2" placeholder="Escribe tu publicación aquí..." @click.stop></textarea>
          </div>
          <!-- Botones-->
          <div class="bg-gray-100 p-3 px-6 justify-end flex gap-4">
            <button @click="hideDialog" class="bg-white border-[1px] border-gray-300 rounded px-4 py-2 text-black cursor-pointer hover:bg-gray-100">Cancelar</button>
            <button class="bg-blue-400 rounded px-4 py-2 text-white cursor-pointer hover:bg-blue-600">Publicar</button>
          </div>
        </div>
      </div>
    </div>

    <!--      Botones para personalizar el Post-->
    <div class="max-w-lg mx-auto">
      <div class="flex flex-row">
        <div class="basis-1/6"></div>
        <div class="basis-2/6 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
          <!-- Icono e texto -->
          Imagen
        </div>
        <div class="basis-2/6 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
          <!-- Icono e texto -->
          Video
        </div>
        <div class="basis-1/6"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const isDialogOpen = ref(false);

function showDialog() {
  isDialogOpen.value = true;
}

function hideDialog() {
  isDialogOpen.value = false;
}
</script>

<style scoped>

</style>
