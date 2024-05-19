<script setup>
import FriendService from '@/services/friend-service.js';
import ListOfSocialUserBy from "@/views/SocialUser/Profile/Lists/ListOfSocialUserBy.vue";

const { userId } = defineProps({
  userId: String,
})
</script>

<template>
  <ListOfSocialUserBy :load-data-fn="FriendService.getFriends" :user-id="userId" />
</template>