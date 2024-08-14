<script setup>
import {ref} from "vue";
import SocialUserVerticalCard from "@/features/UserProfile/components/card/SocialUserVerticalCard.vue";
import InfinityScroll from '@/components/InfinityScroll.vue';
import ListVerticalCardsSkeleton from "@/components/skeletons/ListSocialUserVerticalCardsSkeleton.vue";

const { loadDataFn, userId } = defineProps({
  loadDataFn: Function,
  userId: String,
})

const socialUsers = ref([]);
const loading = ref(false);
const hasMoreFriends = ref(true);

const PAGE_SIZE = 12;
let currentPage = 0;

async function loadSocialUsers() {
  try {
    loading.value = true;

    const usersToAdd = await loadDataFn({
      page: currentPage,
      pageSize: PAGE_SIZE,
      userId,
    });

    // Add the new cards
    socialUsers.value = socialUsers.value.concat(usersToAdd);

    // Check if there are more friends to load
    if (usersToAdd.length < PAGE_SIZE) {
      hasMoreFriends.value = false;
    }

    // increase the counter and prepare it for the next data load
    currentPage++;

    console.log('INFO [ListOfSocialUserBy] Social users to add:', usersToAdd);
  } catch (error) {
    console.error('ERROR [ListOfSocialUserBy] Could not load social users', error);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <InfinityScroll
      class="flex flex-wrap justify-around space-x4"
      :hasMoreElements="hasMoreFriends"
      @LoadMoreElements="loadSocialUsers">
    <SocialUserVerticalCard
        v-for="user in socialUsers"
        :user="user"
        :request=true
        :key="user.id"
        class=""
    />
  </InfinityScroll>
  <ListVerticalCardsSkeleton v-if="loading" :countElements="PAGE_SIZE"/>
</template>