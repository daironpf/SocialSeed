<script setup>

import ProfileView from "@/feed/LeftSide/ProfileView.vue";
import Preferences from "@/feed/LeftSide/Preferences.vue";
import Publications from "@/feed/LeftSide/Publications.vue";
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