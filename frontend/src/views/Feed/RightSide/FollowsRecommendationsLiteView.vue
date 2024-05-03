<script setup>
import {inject, onMounted, ref} from "vue";
import SocialUserToFollowCard from "@/views/SocialUser/Card/SocialUserToFollowCard.vue";
import axios from "axios";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const apiUrl = inject('apiUrl')
const socialUsers = ref([]);
const loading = ref(false);

const PAGE_SIZE = 4;
let currentPage = 0;

async function cargarDatos() {
  try {
    loading.value = true;
    const response = await axios.get(
        `${apiUrl}follow/follow-recommendations/`,
        {
          headers: {
            userId: currentUser.value.id,
          },
          params: {
            page: currentPage,
            size: PAGE_SIZE,
          },
        }
    );

    // Refrescar las tarjetas con N=PAGE_SIZE Nuevas
    socialUsers.value = response.data.response.content;

    console.log('Paginación de usuarios a Seguir', response.data);
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  cargarDatos();
});

async function cargarMasSugerencias() {
  currentPage++; // Incrementa el número de página para cargar más tarjetas
  await cargarDatos();
}
</script>

<template>
  <!--        Sugerencias de Usuarios a Seguir  -->
  <div>
    <div class="flex flex-row justify-between items-center mb-4">
      <p class="font-bold">Usuarios a Seguir</p>
      <button @click="cargarMasSugerencias" :disabled="loading" class="flex items-center focus:outline-none">
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

    <!-- Ver todas las recomendaciones -->
    <div class="font-semibold text-gray-600 mb-4">
      <router-link to="/list/follow-recommendations">
        >> Ver todas las recomendaciones
      </router-link>
    </div>
  </div>
</template>

<style scoped>

</style>