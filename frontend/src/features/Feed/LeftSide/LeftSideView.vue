<script setup>

import ProfileView from "@/features/Feed/LeftSide/ProfileView.vue";
import Preferences from "@/features/Feed/LeftSide/Preferences.vue";
import Publications from "@/features/Feed/LeftSide/Publications.vue";
</script>

<template>
  <div class="basis-1/4  m-1 bg-gray-200">
    <ProfileView :user="'me'"/>

    <Preferences />

    <Publications />
  </div>

</template>

<style scoped>

</style>