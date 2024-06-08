<script setup>
import ListOfSocialUserBy from "@/userProfile/lists/ListOfSocialUserBy.vue";
import FollowService from "@/core/services/follow-service.js";

const { userId } = defineProps({
  userId: String,
})
</script>

<template>
  <ListOfSocialUserBy :load-data-fn="FollowService.getFollows" :user-id="userId" />
</template>