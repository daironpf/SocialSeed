<script setup>
import { ref, inject, onMounted } from "vue";
import LeftSideView from "@/views/LeftSide/LeftSideView.vue";
import RightSideView from "@/views/RightSide/RightSideView.vue";
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
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <LeftSideView />

    <!-- Zona de Posts en el Feed -->
    <div class="basis-1/2 bg-gray-200">

      <div class="flex flex-wrap">
        <SocialUserToFriendCard
            v-for="user in socialUsers"
            :user="user"
            :key="user.id"
            class="w-1/3 m-4"
        />
      </div>



    </div>

    <!-- Lateral Derecho -->
    <RightSideView />
  </div>
</template>

<style scoped>

</style>