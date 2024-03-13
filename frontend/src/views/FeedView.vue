<script setup>
import PostViewInFeed from "@/views/Post/PostViewInFeed.vue";
import CreateNewPostView from "@/views/Post/CreateNewPostView.vue";
import RightSideView from "@/views/RightSide/RightSideView.vue";
import LeftSideView from "@/views/LeftSide/LeftSideView.vue";
</script>

<template>
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <LeftSideView />

    <!-- Zona de Posts en el Feed -->
    <div class="basis-1/2 bg-gray-200">
      <CreateNewPostView />

      <!--      Zona de Publicaciones del Feed-->
      <PostViewInFeed />
    </div>

    <!-- Lateral Derecho -->
    <RightSideView />
  </div>
</template>

<style scoped>

</style>