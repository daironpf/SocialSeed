<script setup>
import ProfileView from "@/views/LeftSide/ProfileView.vue";
import Preferences from "@/views/LeftSide/Preferences.vue";
import Publications from "@/views/LeftSide/Publications.vue";

const props = defineProps({
  userId: String,
})
</script>

<template>
  <div class="basis-1/4  m-1 bg-gray-200">
    <h1>{{userId}}</h1>
    <ProfileView />

    <Preferences />

    <Publications />
  </div>

</template>

<style scoped>

</style>