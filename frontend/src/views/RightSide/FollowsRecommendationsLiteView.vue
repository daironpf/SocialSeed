<script setup>
import {inject, onMounted, ref} from "vue";
import SocialUserToFollowCard from "@/views/SocialUser/SocialUserToFollowCard.vue";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const apiUrl = inject('apiUrl')
const socialUsers = ref([]);
const loading = ref(false);

async function cargarDatos() {
  try {
    loading.value = true;
    const response = await fetch(apiUrl + 'follow/follow-recommendations-lite/' + currentUser.value.id);

    await new Promise(resolve => setTimeout(resolve, 1000));

    const data = await response.json();
    socialUsers.value = data.response;
    console.log('Recomendacion de usuarios a Seguir', data)
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
  <!--        Sugerencias de Usuarios a Seguir  -->
  <div>
    <div class="flex flex-row justify-between items-center mb-4">
      <p class="font-bold">Usuarios a Seguir</p>
      <button @click="recargarSugerencias" :disabled="loading" class="flex items-center focus:outline-none">
        <fa icon="fa-solid fa-sync" class="text-gray-500" :class="{ 'animate-spin': loading }" />
      </button>
    </div>

    <!-- Lista de Sugerencias a Seguir -->
    <div v-if="!loading">
      <SocialUserToFollowCard
          v-for="user in socialUsers"
          :user="user"
          :key="user.id"
      />
    </div>
    <div v-else class="text-gray-600">Cargando...</div>

    <div class="font-semibold text-gray-600">>> Ver todas las recomendaciones</div>
  </div>
</template>

<style scoped>

</style>