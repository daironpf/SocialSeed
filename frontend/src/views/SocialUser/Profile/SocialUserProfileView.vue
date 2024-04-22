<script setup>
import RightSideView from "@/views/RightSide/RightSideView.vue";
import { useRoute } from 'vue-router'
import LeftSideSocialUserProfile from "@/views/SocialUser/Profile/LeftSideSocialUserProfile.vue";
import {ref} from "vue";
import ListOfFriendsView from "@/views/SocialUser/Profile/Lists/ListOfFriendsView.vue";
import ListOfPostsView from "@/views/SocialUser/Profile/Lists/ListOfPostsView.vue";
import ListOfFollowsView from "@/views/SocialUser/Profile/Lists/ListOfFollowsView.vue";
import ListOfFollowersView from "@/views/SocialUser/Profile/Lists/ListOfFollowersView.vue";

const route = useRoute()
const userId = route.params.id

const activeTab = ref('posts');

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

      <div class="basis-1/2 bg-white mb-2 p-3 ml-10 mr-10 flex justify-around items-center">
        <!-- Botón de Publicaciones -->
        <div :class="{ 'active-tab': activeTab === 'posts' }" class="text-center relative">
          <p @click="activeTab = 'posts'"
             class="font-bold text-gray-700">Publicaciones</p>
          <div v-if="activeTab === 'posts'" class="border-b-2 border-blue-500 absolute bottom-0 left-0 right-0"></div>
        </div>

        <!-- Botón de Amigos -->
        <div :class="{ 'active-tab': activeTab === 'friends' }" class="text-center relative">
          <p @click="activeTab = 'friends'"
             class="font-bold text-gray-700">Amigos</p>
          <div v-if="activeTab === 'friends'" class="border-b-2 border-blue-500 absolute bottom-0 left-0 right-0"></div>
        </div>

        <!-- Botón de Seguidos -->
        <div :class="{ 'active-tab': activeTab === 'follows' }" class="text-center relative">
          <p @click="activeTab = 'follows'"
             class="font-bold text-gray-700">Seguidos</p>
          <div v-if="activeTab === 'follows'" class="border-b-2 border-blue-500 absolute bottom-0 left-0 right-0"></div>
        </div>

        <!-- Botón de Seguidores -->
        <div :class="{ 'active-tab': activeTab === 'followers' }" class="text-center relative">
          <p @click="activeTab = 'followers'"
             class="font-bold text-gray-700">Seguidores</p>
          <div v-if="activeTab === 'followers'" class="border-b-2 border-blue-500 absolute bottom-0 left-0 right-0"></div>
        </div>
      </div>

      <div class="profile-content">
        <div v-if="activeTab === 'friends'">
          <!-- Renderizar lista de amigos -->
          <ListOfFriendsView :userId="userId" />
        </div>
        <div v-else-if="activeTab === 'follows'">
          <!-- Renderizar lista de seguidores -->
          <ListOfFollowsView :userId="userId" />
        </div>
        <div v-else-if="activeTab === 'posts'">
          <!-- Renderizar lista de publicaciones -->
          <ListOfPostsView :userId="userId" />
        </div>
        <div v-else-if="activeTab === 'followers'">
          <!-- Renderizar lista de seguidores -->
          <ListOfFollowersView :userId="userId" />
        </div>
      </div>
    </div>

    <!-- Lateral Derecho -->
    <RightSideView/>
  </div>
</template>

<style scoped>
.active-tab {
  color: #4CAF50; /* Color activo */
}

.active-tab:hover {
  cursor: pointer;
  opacity: 0.8; /* Reducir opacidad al pasar el mouse */
}

/* Estilo para el borde inferior */
.border-b-2 {
  border-bottom-width: 2px;
}
</style>

