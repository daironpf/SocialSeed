<script setup>
const props = defineProps({
  hashtag: Object,
})

import BaseTooltip from "@/components/BaseTooltip.vue";
</script>

<template>
  <div class="flex justify-between">
    <div class="">
      <p class="font-bold text-lg">#{{ hashtag.name }} </p>
    </div>

    <div class="flex items-center">
      <BaseTooltip
          :content="`Total de publicaciones hechas con este HashTag: ${props.hashtag.postTaggedIn}`"
          placement="bottom">
        <p class="p-2">
          <fa icon="fa-solid fa-pen-to-square text-blue-300" />
          <span class="font-semibold">{{ props.hashtag.postTaggedIn }}</span>
        </p>
      </BaseTooltip>
      <BaseTooltip
          :content="`Total de Usuarios interesados en este HashTag: ${props.hashtag.socialUserInterestIn}`"
          placement="bottom">
      <p >
        <fa icon="fa-solid fa-user text-blue-300" />
        <span class="font-semibold">{{ props.hashtag.socialUserInterestIn }}</span>
      </p>
      </BaseTooltip>
    </div>
  </div>
</template>



<style scoped>

</style>