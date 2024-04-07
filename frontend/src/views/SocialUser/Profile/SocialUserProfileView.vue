<script setup>

import LeftSideView from "@/views/LeftSide/LeftSideView.vue";
import RightSideView from "@/views/RightSide/RightSideView.vue";
import CreateNewPostView from "@/views/Post/CreateNewPostView.vue";
import PostViewInFeed from "@/views/Post/PostViewInFeed.vue";
</script>

<template>
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <LeftSideView />

    <!-- Zona de Posts en el Feed -->
    <div class="basis-1/2 bg-gray-200">
     
    </div>

    <!-- Lateral Derecho -->
    <RightSideView />
  </div>
</template>

<style scoped>

</style>