<script setup>
import CreateNewPostView from "@/posts/CreateNewPostView.vue";
import RightSideView from "@/feed/RightSide/RightSideView.vue";
import LeftSideView from "@/feed/LeftSide/LeftSideView.vue";
import FeedItem from "@/feed/FeedItem.vue";
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