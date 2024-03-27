<script setup>
import {inject, onMounted, ref} from "vue";
import SocialUserToFollowCard from "@/views/SocialUser/SocialUserToFollowCard.vue";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const apiUrl = inject('apiUrl')

const socialUsers = ref([]);

async function cargarDatos() {
  try {
    const response = await fetch(apiUrl + 'follow/follow-recommendations-lite/' + currentUser.value.id);
    const data = await response.json();

    socialUsers.value = data.response;
    console.log('Recomendacion de usuarios a Seguir', data)

  } catch (e) {
    console.error(e);
  }
}

onMounted(() => {
  cargarDatos();
});
</script>

<template>
  <!--        Sugerencias de Usuarios a Seguir  -->
  <div>
    <div class="flex flex-row justify-between mt-5 b-2">

      <p class="font-bold">Usuarios a Seguir</p>
      <p>
        <fa icon="fa-solid fa-sync" class="text-gray-500 "/>
      </p>
    </div>

    <SocialUserToFollowCard
        v-for="user in socialUsers"
        :user="user"
        :key="user.id"
    />

    <a href="">Ver todas las recomendaciones</a>
  </div>
</template>

<style scoped>

</style>