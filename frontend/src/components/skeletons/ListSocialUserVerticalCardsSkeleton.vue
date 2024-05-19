<script setup>
import CardSkeleton from "@/components/skeletons/SocialUserVerticalCardSkeleton.vue";

const { countElements = 0 } = defineProps({ countElements: Number })
</script>

<template>
  <div class="flex flex-wrap justify-around space-x4">
    <CardSkeleton v-for="i in countElements" />
  </div>
</template>