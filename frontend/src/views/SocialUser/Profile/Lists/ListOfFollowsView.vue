<script setup>
import ListOfSocialUserBy from "@/views/SocialUser/Profile/Lists/ListOfSocialUserBy.vue";
import FollowService from "@/services/follow-service.js";

const { userId } = defineProps({
  userId: String,
})
</script>

<template>
  <ListOfSocialUserBy :load-data-fn="FollowService.getFollows" :user-id="userId" />
</template>