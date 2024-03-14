<script setup>
import SocialUserRelationshipCard from "@/views/SocialUser/SocialUserRelationshipCard.vue";
import {inject, onMounted, ref} from "vue";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const apiUrl = inject('apiUrl')

const socialUsers = ref([]);

async function cargarDatos() {
  try {
    const response = await fetch(apiUrl + 'friend/friend-recommendations-lite/' + currentUser.value.id);
    const data = await response.json();

    socialUsers.value = data.response;
    console.log('Recomendacion de Amigos', data)

  } catch (e) {
    console.error(e);
  }
}

onMounted(() => {
  cargarDatos();
});
</script>

<template>
  <!--        Sugerencias de Amistad-->
  <div>
    <div class="flex flex-row justify-between">

      <p class="font-bold">Pedir Amistad</p>
      <p>
        <fa icon="fa-solid fa-sync" class="text-gray-500 "/>
      </p>
    </div>

    <SocialUserRelationshipCard
        v-for="user in socialUsers"
        :user="user"
        :key="user.id"
    />

    <a href="">Ver todas las recomendaciones</a>
  </div>
</template>

<style scoped>

</style>