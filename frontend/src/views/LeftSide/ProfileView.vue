<script setup>
import {ref} from "vue";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
console.log('Usuario Actual: ', currentUser.value);

</script>

<template>
  <div class="rounded-lg shadow w-30 bg-gray-50">
    <!--        Perfil-->
    <div class="bg-cover  h-20 rounded-t-lg" style="background-image: url('/img/background-user-test.jpg')"></div>
    <div class="p-4">
      <img :src="currentUser.profileImage" class="rounded-full w-16 h-16 border border-white border-2 -mt-11 mx-auto" alt="">
      <h1 class="text-xl font-bold text-color-black text-center mt-2">{{ currentUser.fullName }}</h1>
      <p class="text-gray-700 text-center">{{ currentUser.bio }}</p>
    </div>
  </div>
</template>

<style scoped>

</style>