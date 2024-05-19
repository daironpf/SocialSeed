<script setup>
import {ref} from "vue";
import SocialUserVerticalCard from "@/views/SocialUser/Card/SocialUserVerticalCard.vue";
import {getCurrentUser} from '@/services/local-storage.js';
import InfinityScroll from '@/views/InfinityScroll.vue';
import ListVerticalCardsSkeleton from "@/views/ListVerticalCardsSkeleton.vue";
import FriendService from '@/services/friend-service.js';

const props = defineProps({
  userId: String,
})

const currentUser = ref(getCurrentUser());
const socialUsers = ref([]);
const loading = ref(false);
const hasMoreFriends = ref(true);

const PAGE_SIZE = 10;
let currentPage = 0;

async function loadFriends() {
  try {
    loading.value = true;

    const newFriends = await FriendService.getFriends({
      currentPage,
      pageSize: PAGE_SIZE,
      userId: currentUser.value.id,
    });

    // Add the new cards
    socialUsers.value = socialUsers.value.concat(newFriends);

    // Check if there are more friends to load
    if (newFriends.length < PAGE_SIZE) {
      hasMoreFriends.value = false;
    }

    // increase the counter and prepare it for the next data load
    currentPage++;

    console.log('INFO [ListOfFriendsView] Response Friends: ', newFriends);
  } catch (error) {
    console.error('ERROR [ListOfFriendsView] Could not load friends', error);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <InfinityScroll
      class="flex flex-wrap justify-around space-x4"
      :hasMoreElements="hasMoreFriends"
      @LoadMoreElements="loadFriends">
    <SocialUserVerticalCard
        v-for="user in socialUsers"
        :user="user"
        :request=true
        :key="user.id"
        class=""
    />
  </InfinityScroll>
  <ListVerticalCardsSkeleton v-if="loading"/>
</template>