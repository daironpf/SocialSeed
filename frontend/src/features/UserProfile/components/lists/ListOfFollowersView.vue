<script setup>
import FollowService from "@/core/a mover/follow-service.ts";
import ListOfSocialUserBy from "@/features/UserProfile/components/lists/ListOfSocialUserBy.vue";

const { userId } = defineProps({
  userId: String,
})
</script>

<template>
  <ListOfSocialUserBy :load-data-fn="FollowService.getFollowers" :user-id="userId" />
</template>