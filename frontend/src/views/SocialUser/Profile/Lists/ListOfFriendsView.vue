<script setup>
import axios from "axios";
import {inject, onMounted, ref} from "vue";
import SocialUserVerticalCard from "@/views/SocialUser/Card/SocialUserVerticalCard.vue";

const props = defineProps({
  userId: String,
})

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
        `${apiUrl}friend/friendsOf/${props.userId}`,
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

    // socialUsers.value = response.data.response.content;
    // Agregar las nuevas tarjetas a la lista existente
    socialUsers.value = [...socialUsers.value, ...response.data.response.content];

    console.log('Amigos', response.data);
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
  currentPage++;
  await cargarDatos();
}

</script>

<template>
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
</template>

<style scoped>

</style>