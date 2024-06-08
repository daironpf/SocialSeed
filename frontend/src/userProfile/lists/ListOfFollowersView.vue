<script setup>
import FollowService from "@/core/services/follow-service.js";
import ListOfSocialUserBy from "@/userProfile/lists/ListOfSocialUserBy.vue";

const { userId } = defineProps({
  userId: String,
})
</script>

<template>
  <ListOfSocialUserBy :load-data-fn="FollowService.getFollowers" :user-id="userId" />
</template>