<script setup>
import ProfileView from "@/views/LeftSide/ProfileView.vue";
import Preferences from "@/views/LeftSide/Preferences.vue";
import Publications from "@/views/LeftSide/Publications.vue";
import {inject, onMounted, ref} from "vue";
import axios from "axios";

const props = defineProps({
  userId: String,
})

</script>

<template>
  <div class="basis-1/4  m-1 bg-gray-200">
    <ProfileView :user="props.userId" />

    <Preferences />

    <Publications />
  </div>

</template>

<style scoped>

</style>