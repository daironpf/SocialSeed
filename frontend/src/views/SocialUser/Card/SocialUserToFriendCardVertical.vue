<script setup>
const props = defineProps({
  user: Object,
  request: Boolean
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
          <router-link :to="{ name: 'su-profile', params: { id: user.id }}">
            <h2 class="text-md font-semibold mb-1">{{user.fullName}}</h2>
            <p class="text-sm text-gray-600 ">@{{user.userName}}</p>
          </router-link>
          <span class="text-slate-700 dark:text-slate-500" >
            <span class="font-semibold">23</span> amigos en común
          </span>
          <div>
            <span class="text-sm font-semibold ">Te sigue</span>
          </div>
          <div>
            <span class="text-sm font-semibold ">Es tu Amigo</span>
          </div>
        </div>
      </div>

      <div v-if="props.request"
          class="mt-2 mb-2">
        <!-- Botón de Pedir Amistad -->
        <button class="bg-blue-300 hover:bg-blue-500 text-white font-semibold
        p-2 m-2 rounded-lg w-30 h-15 focus:outline-none focus:shadow-outline">
          <fa icon="fa-solid fa-user-plus" class="text-white-600"/>
          Amistad
        </button>

        <!-- Botón de Seguir -->
        <button class="bg-blue-300 hover:bg-blue-500 text-white font-semibold
          p-2 rounded-lg w-30 focus:outline-none focus:shadow-outline">
          <fa icon="fa-solid fa-person-circle-plus" class="text-white-600"/>
          Seguir
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>