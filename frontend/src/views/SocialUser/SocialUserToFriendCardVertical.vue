<script setup>
const props = defineProps({
  user: Object,
})
</script>

<template>
<!--  Sugerencia N -->
  <div class="bg-gray-50 rounded-lg shadow-md w-64 pl-3 pr-3 pt-4 pb-3 item-center m-1">
    <!-- Foto de Perfil -->
    <div class="flex items-center flex-col">
      <!-- Información del Usuario -->
      <div class="flex items-center flex-col">
        <img :src="user.profileImage"
             alt="Foto de usuario"
             class="w-16 h-16 rounded-full mr-4 mb-2">
        <div>
          <h2 class="text-md font-semibold mb-1">{{user.fullName}}</h2>
          <p class="text-sm text-gray-600 ">@{{user.userName}}</p>
          <span class="text-slate-700 dark:text-slate-500" >
            <span class="font-semibold">23</span> amigos en común
          </span>
        </div>
      </div>

      <div class="mt-4 mb-2">
        <!-- Botón de Pedir Amistad -->
        <button class="bg-blue-300 hover:bg-blue-500 text-white font-semibold
        py-2 px-4 rounded-lg w-30 h-15 focus:outline-none focus:shadow-outline">
          <fa icon="fa-solid fa-user-plus" class="text-white-600"/>
          Amistad
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>