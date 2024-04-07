<script setup>

import LeftSideView from "@/views/LeftSide/LeftSideView.vue";
import RightSideView from "@/views/RightSide/RightSideView.vue";

import { useRoute } from 'vue-router'
import LeftSideSocialUserProfile from "@/views/SocialUser/Profile/LeftSideSocialUserProfile.vue";

const route = useRoute()
const userId = route.params.id
</script>

<template>
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <LeftSideSocialUserProfile :userId="userId" />

    <!-- Zona Central -->
    <div class="basis-1/2 bg-gray-200">
        Trabajando
    </div>

    <!-- Lateral Derecho -->
    <RightSideView/>
  </div>
</template>

<style scoped>

</style>