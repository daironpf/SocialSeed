<script setup>
const props = defineProps({
  hashtag: Object,

})
</script>

<template>
  <div class="flex justify-between">
    <div class="">
      <p class="font-bold text-lg">#{{ hashtag.name }} </p>
    </div>
    <div class="flex items-center">
      <p class="p-2"> <fa icon="fa-solid fa-pen-to-square text-blue-300" /> <span class="font-semibold">{{ hashtag.postTaggedIn }}</span> </p>
      <p > <fa icon="fa-solid fa-user text-blue-300" /> <span class="font-semibold">{{ hashtag.socialUserInterestIn }}</span> </p>
    </div>
  </div>
</template>



<style scoped>

</style>