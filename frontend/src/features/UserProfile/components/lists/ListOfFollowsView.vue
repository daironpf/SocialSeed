<script setup>
import ListOfSocialUserBy from "@/features/UserProfile/components/lists/ListOfSocialUserBy.vue";
import FollowService from "@/core/a mover/follow-service.ts";

const { userId } = defineProps({
  userId: String,
})
</script>

<template>
  <ListOfSocialUserBy :load-data-fn="FollowService.getFollows" :user-id="userId" />
</template>