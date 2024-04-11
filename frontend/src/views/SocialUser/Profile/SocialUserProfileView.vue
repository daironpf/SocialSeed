<script setup>

import LeftSideView from "@/views/LeftSide/LeftSideView.vue";
import RightSideView from "@/views/RightSide/RightSideView.vue";

import { useRoute } from 'vue-router'
import LeftSideSocialUserProfile from "@/views/SocialUser/Profile/LeftSideSocialUserProfile.vue";
import ListOfFollowRecommendations from "@/views/Lists/ListOfFollowRecommendations.vue";
import ListOfFriendsRecommendations from "@/views/Lists/ListOfFriendsRecommendations.vue";
import {ref} from "vue";

const route = useRoute()
const userId = route.params.id



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
          <p class="font-bold text-gray-700">Publicaciones</p>
        </div>

        <!-- Botón de Amigos -->
        <div class="text-center">
          <p class="font-bold text-gray-700">Amigos</p>
        </div>

        <!-- Botón de Seguidos -->
        <div class="text-center">
          <p class="font-bold text-gray-700">Seguidos</p>
        </div>

        <!-- Botón de Seguidores -->
        <div class="text-center">
          <p class="font-bold text-gray-700">Seguidores</p>
        </div>
      </div>
    </div>

    <!-- Lateral Derecho -->
    <RightSideView/>
  </div>
</template>

<style scoped>

</style>