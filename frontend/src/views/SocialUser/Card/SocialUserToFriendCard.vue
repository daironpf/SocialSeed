<script setup>
const props = defineProps({
  user: Object,
})
</script>

<template>
<!--  Sugerencia N -->
  <div class="bg-gray-50 rounded-lg shadow-md mx-auto mt-4 p-4 max-w-lg">
    <!-- Foto de Perfil -->
    <div class="flex items-center justify-between">
      <!-- Información del Usuario -->
      <div class="flex items-center">
        <img :src="user.profileImage" alt="Foto de usuario" class="w-16 h-16 rounded-full mr-4">
        <div>
          <router-link :to="{ name: 'su-profile', params: { id: user.id }}">
            <h2 class="text-md font-semibold">{{user.fullName}}</h2>
            <p class="text-sm text-gray-600">@{{user.userName}}</p>
          </router-link>
          <span class="text-sm text-gray-600"><span class="font-semibold">23</span> amigos en común</span>
        </div>
      </div>
      <div>
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