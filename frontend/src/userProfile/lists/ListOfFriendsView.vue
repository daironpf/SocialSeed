<script setup>
import FriendService from '@/core/services/friend-service.js';
import ListOfSocialUserBy from "@/userProfile/lists/ListOfSocialUserBy.vue";

const { userId } = defineProps({
  userId: String,
})
</script>

<template>
  <ListOfSocialUserBy :load-data-fn="FriendService.getFriends" :user-id="userId" />
</template>