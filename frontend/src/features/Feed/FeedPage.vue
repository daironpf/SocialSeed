<script setup lang="ts">
import CreateNewPostView from "@/features/Posts/CreateNewPostView.vue";
import RightSideView from "@/features/Feed/RightSide/RightSideView.vue";
import LeftSideView from "@/features/Feed/LeftSide/LeftSideView.vue";
import FeedItem from "@/features/Feed/FeedItem.vue";
</script>

<template>
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <LeftSideView />

    <!-- Zona de Posts en el Feed -->
    <div class="basis-1/2 bg-gray-200">
      <CreateNewPostView />

      <!--      Zona de Publicaciones del Feed-->
      <FeedItem />
    </div>

    <!-- Lateral Derecho -->
    <RightSideView />
  </div>
</template>

<style scoped>

</style>