<script setup>
import {ref} from "vue";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));

function showDialog(){
  let dialog = document.getElementById("dialog");
  dialog.classList.remove('hidden');
  dialog.classList.add('flex');
  setTimeout(()=>{
    dialog.classList.add('opacity-100');
  }, 20);
}

function hideDialog() {
  let dialog = document.getElementById("dialog");
  dialog.classList.add('opacity-0');
  dialog.classList.remove('opacity-100');

  setTimeout(()=>{
    dialog.classList.add('hidden');
    dialog.classList.remove('flex');
  }, 500);
}
</script>

<template>
  <div class="bg-gray-50 rounded-lg shadow m-1 ml-10 mr-10 p-2">
    <div class="max-w-lg mx-auto flex items-center border-b border-gray-300 pb-4 mb-4">
      <img :src="currentUser.profileImage" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
      <button
          onclick="showDialog()"
          class="flex-1 px-4 py-2 bg-gray-50 text-gray-600 rounded border border-gray-400 shadow-md
      hover:bg-gray-100
      focus:outline-none focus:bg-gray-100">
        Crear publicación
      </button>

      <div
          onclick="hideDialog()"
          id="dialog"
          class="fixed left-0 top-0 bg-black bg-opacity-50 w-screen h-screen justify-center item-center
            opacity-0 hidden transition-opacity duration-500">
        <div
            onclick="event.stopImmediatePropagation()"
            class="bg-white rounded shadow-md w-[25%] h-[30%] mt-[10%] flex gap-5 flex-col overflow-hidden">
          <!-- Contenido -->
            <div class="flex-grow">
              <h1 class="font-bold text-center text-lg mb-2 text-gray-700">Create Post</h1>
              <p class="text-gray-600">
                Are you sure you want to deactive your account? All of your data
                will be permanently removed. This action cannot be undone.
              </p>
            </div>
          <!-- Botones-->
          <div class="bg-gray-100 p-3 px-6 justify-end flex gap-4">
            <button
                onclick="hideDialog()"
                class="bg-white border-[1px] border-gray-300 rounded px-4 py-2 text-black cursor-pointer hover:bg-gray-100">Cancel</button>
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
          <fa icon="fa-solid fa-image" class="text-blue-500"/>
          <p class="ml-2">Imagen</p>
        </div>
        <div class="basis-2/6 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
          <fa icon="fa-solid fa-clock" class="text-blue-500"/>
          <p class="ml-2">Programar</p>
        </div>
        <div class="basis-1/6"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>