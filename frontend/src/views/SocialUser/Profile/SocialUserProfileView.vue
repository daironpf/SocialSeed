<script setup>

import LeftSideView from "@/views/LeftSide/LeftSideView.vue";
import RightSideView from "@/views/RightSide/RightSideView.vue";

import { useRoute } from 'vue-router'
import LeftSideSocialUserProfile from "@/views/SocialUser/Profile/LeftSideSocialUserProfile.vue";
import ListOfFollowRecommendations from "@/views/Lists/ListOfFollowRecommendations.vue";
import ListOfFriendsRecommendations from "@/views/Lists/ListOfFriendsRecommendations.vue";
import {onMounted, ref} from "vue";
import {arrow, computePosition, flip, offset, shift} from "@floating-ui/dom";

const route = useRoute()
const userId = route.params.id


const referenceRef = ref();
const floatingRef = ref();
const arrowRef = ref();


onMounted(async () => {
  const {x, y, middlewareData, placement} = await computePosition(referenceRef.value, floatingRef.value, {
    placement: "bottom",
    middleware: [offset(8), flip(), shift({padding: 5}), arrow({element: arrowRef.value})]
  });

  Object.assign(floatingRef.value.style, {
    left: `${x}px`,
    top: `${y}px`,
  });

  const {x: arrowX, y: arrowY} = middlewareData.arrow;

  Object.assign(arrowRef.value.style, {
    left: arrowX ? `${arrowX}px`: "",
    top: arrowY ? `${arrowY}px`: "",
  })
});
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

        <button
            ref="referenceRef"
            class="bg-rose-600 hover:bg-rose-500 text-white font-medium p-2.5 rounded-md leading-none">Click me</button>
        <div
            ref="floatingRef"
            class="absolute top-0 left-0 z-50
         bg-gray-700 text-sm text-white px-3 py-1.5 rounded-md cursor-default">Esto es un botón

          <div class="bg-red-600 h-[8px] w-[8px] rotate-45" ref="arrowRef"></div>
        </div>


    </div>

    <!-- Lateral Derecho -->
    <RightSideView/>
  </div>
</template>

<style scoped>

</style>