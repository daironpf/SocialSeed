<script setup>
import RightSideView from "@/views/RightSide/RightSideView.vue";
import { useRoute } from 'vue-router'
import LeftSideSocialUserProfile from "@/views/SocialUser/Profile/LeftSideSocialUserProfile.vue";
import {ref} from "vue";
import ListOfFriendsView from "@/views/SocialUser/Profile/Lists/ListOfFriendsView.vue";
import ListOfPostsView from "@/views/SocialUser/Profile/Lists/ListOfPostsView.vue";

const route = useRoute()
const userId = route.params.id

const activeTab = ref('friends');

const user = {
  // Datos del usuario
  followers: ['Seguidor 1', 'Seguidor 2', 'Seguidor 3'],
  posts: ['Publicación 1', 'Publicación 2', 'Publicación 3']
};
</script>

<template>
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <LeftSideSocialUserProfile :userId="userId" />

    <!-- Zona Central -->
    <div class="basis-1/2 bg-gray-200">

      <div class="basis-1/2 bg-gray-200 flex justify-around items-center">
        <!-- Botón de Publicaciones -->
        <div class="text-center">
          <p @click="activeTab = 'posts'"
              class="font-bold text-gray-700">Publicaciones</p>
        </div>

        <!-- Botón de Amigos -->
        <div class="text-center">
          <p @click="activeTab = 'friends'"
             class="font-bold text-gray-700">Amigos</p>
        </div>

        <!-- Botón de Seguidos -->
        <div class="text-center">
          <p class="font-bold text-gray-700">Seguidos</p>
        </div>

        <!-- Botón de Seguidores -->
        <div class="text-center">
          <p @click="activeTab = 'followers'"
              class="font-bold text-gray-700">Seguidores</p>
        </div>
      </div>

      <div class="profile-content">
        <div v-if="activeTab === 'friends'">
          <!-- Renderizar lista de amigos -->
          <ListOfFriendsView :userId="userId" />
        </div>
        <div v-else-if="activeTab === 'followers'">
          <!-- Renderizar lista de seguidores -->
          <ul>
            <li v-for="follower in user.followers" :key="follower">{{ follower }}</li>
          </ul>
        </div>
        <div v-else-if="activeTab === 'posts'">
          <!-- Renderizar lista de publicaciones -->
          <ListOfPostsView :userId="userId" />
        </div>
      </div>
    </div>

    <!-- Lateral Derecho -->
    <RightSideView/>
  </div>
</template>

<style scoped>

</style>