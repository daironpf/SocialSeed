<script setup>
import { ref, inject, onMounted } from "vue";
import LeftSideView from "@/views/LeftSide/LeftSideView.vue";
import RightSideView from "@/views/RightSide/RightSideView.vue";
import SocialUserToFriendCardVertical from "@/views/SocialUser/SocialUserToFriendCardVertical.vue";
import axios from "axios";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const apiUrl = inject('apiUrl')
const socialUsers = ref([]);
const loading = ref(false);

async function cargarDatos() {
  try {
    loading.value = true;
    const response = await axios.get(
        `${apiUrl}friend/friend-recommendations/`,
        {
          headers: {
            userId: currentUser.value.id,
          },
          params: {
            page: 0,
            size: 12,
          },
        }
    );

    await new Promise(resolve => setTimeout(resolve, 1000));

    socialUsers.value = response.data.response.content;
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

async function recargarSugerencias() {
  await cargarDatos();
  console.log('por recargar sugerencia: ',loading.value)
}
</script>

<template>
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <LeftSideView />

    <!-- Zona de Posts en el Feed -->
    <div class="basis-1/2 bg-gray-200">

      <div class="flex flex-wrap justify-around space-x4">
        <SocialUserToFriendCardVertical
            v-for="user in socialUsers"
            :user="user"
            :key="user.id"
            class=""
        />
      </div>
    </div>

    <!-- Lateral Derecho -->
    <RightSideView />
  </div>
</template>

<style scoped>

</style>