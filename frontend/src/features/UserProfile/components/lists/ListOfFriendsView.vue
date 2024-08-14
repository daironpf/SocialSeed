<script setup>
import FriendService from '@/core/a mover/friend-service.ts';
import ListOfSocialUserBy from "@/features/UserProfile/components/lists/ListOfSocialUserBy.vue";

const { userId } = defineProps({
  userId: String,
})
</script>

<template>
  <ListOfSocialUserBy :load-data-fn="FriendService.getFriends" :user-id="userId" />
</template>