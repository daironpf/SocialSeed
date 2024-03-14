<script setup>
const props = defineProps({
  user: Object,
})


</script>

<template>
  <!--      Sugerencia N -->
  <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2 mt-2">
    <!--        Zona de Recomendaciones para Amigos-->
    <div class="flex flex-row">
      <!--          Encabezado-->
      <img :src="user.profileImage" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
      <div>
        <div>
          <p>{{user.fullName}}</p>
          <span class="text-xs">@{{user.userName}}</span>
        </div>
        <div class="flex pt-3">
          <!-- Botonera -->
          <span class="ml-2 mr-2 bg-sky-200">Pedir Amistad</span>
          <span class="ml-2 mr-2 bg-sky-200"> C </span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>