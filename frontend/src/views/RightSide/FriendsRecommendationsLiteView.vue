<script setup>

import { ref, inject, onMounted } from "vue";
import SocialUserToFriendCard from "@/views/SocialUser/SocialUserToFriendCard.vue";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const apiUrl = inject('apiUrl')
const socialUsers = ref([]);
const loading = ref(false);

async function cargarDatos() {
  try {
    loading.value = true;
    const response = await fetch(apiUrl + 'friend/friend-recommendations-lite/' + currentUser.value.id);

    await new Promise(resolve => setTimeout(resolve, 1000));

    const data = await response.json();
    socialUsers.value = data.response;
    console.log('Recomendacion de Amigos', data);
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  cargarDatos();
});

async function recargarSugerencias() {
  await cargarDatos();
  console.log('por recargar sugerencia: ',loading.value)
}
</script>

<template>
  <!-- Sugerencias de Amistad -->
  <div>
    <div class="flex flex-row justify-between items-center mb-4">
      <p class="font-bold">Pedir Amistad</p>
      <button @click="recargarSugerencias" :disabled="loading" class="flex items-center focus:outline-none">
        <fa icon="fa-solid fa-sync" class="text-gray-500" :class="{ 'animate-spin': loading }" />
      </button>
    </div>

    <!-- Lista de Sugerencias de Amistad -->
    <div v-if="!loading">
      <SocialUserToFriendCard
          v-for="user in socialUsers"
          :user="user"
          :key="user.id"
      />
    </div>
    <div v-else class="text-gray-600">Cargando...</div>

    <!-- Ver todas las recomendaciones -->
    <div class="font-semibold text-gray-600">>> Ver todas las recomendaciones</div>
  </div>
</template>

<style>
/* Estilos opcionales para animar el icono de carga */
@keyframes spin {
  to { transform: rotate(360deg); }
}
.animate-spin {
  animation: spin 1s linear infinite;
}
</style>
