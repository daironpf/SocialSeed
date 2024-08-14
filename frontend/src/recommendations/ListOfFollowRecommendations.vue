<script setup>
import { ref, inject, onMounted } from "vue";
import LeftSideView from "@/feed/LeftSide/LeftSideView.vue";
import RightSideView from "@/feed/RightSide/RightSideView.vue";
import SocialUserVerticalCard from "@/features/UserProfile/components/card/SocialUserVerticalCard.vue";
import axios from "axios";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const apiUrl = inject('apiUrl')
const socialUsers = ref([]);
const loading = ref(false);

const PAGE_SIZE = 9;
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

    await new Promise(resolve => setTimeout(resolve, 1));

    // socialUsers.value = response.data.response.content;
    // Agregar las nuevas tarjetas a la lista existente
    socialUsers.value = [...socialUsers.value, ...response.data.response.content];

    console.log('Paginación de Amigos', response.data);
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
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <LeftSideView />

    <!-- Zona Central -->
    <div class="basis-1/2 bg-gray-200">
      <h2 class="text-center font-bold mb-3 text-xl ">
        Recomendaciones de SocialUser a Seguir
      </h2>

      <div class="flex flex-wrap justify-around space-x4">
        <SocialUserVerticalCard
            v-for="user in socialUsers"
            :user="user"
            :request=true
            :key="user.id"
            class=""
        />
      </div>

      <!-- Botón para cargar más sugerencias -->
      <div class="flex items-center justify-center mt-5">
        <button @click="cargarMasSugerencias" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
          Cargar más sugerencias
        </button>
      </div>

    </div>

    <!-- Lateral Derecho -->
    <RightSideView />
  </div>
</template>

<style scoped>

</style>