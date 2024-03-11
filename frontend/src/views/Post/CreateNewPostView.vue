<script setup>

</script>

<template>
  <div class="bg-gray-50 rounded-lg shadow m-1 ml-10 mr-10 p-2">
    <div class="max-w-lg mx-auto flex items-center border-b border-gray-300 pb-4 mb-4">
      <img src="/img/user-perfil.jpg" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
      <button class="flex-1 px-4 py-2 bg-gray-50 text-gray-500 rounded-full border border-gray-400 hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
        Crear publicación
      </button>
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