<script setup>
import FollowService from "@/services/follow-service.js";
import ListOfSocialUserBy from "@/views/SocialUser/Profile/Lists/ListOfSocialUserBy.vue";

const { userId } = defineProps({
  userId: String,
})
</script>

<template>
  <ListOfSocialUserBy :load-data-fn="FollowService.getFollowers" :user-id="userId" />
</template>