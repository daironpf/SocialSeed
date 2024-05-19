<script setup>
import axios from "axios";
import {inject, ref} from "vue";
import SocialUserVerticalCard from "@/views/SocialUser/Card/SocialUserVerticalCard.vue";
import { getCurrentUser } from '@/services/local-storage.js';
import InfinityScroll from '@/views/InfinityScroll.vue';
import CardSkeleton from "@/views/VerticalCardSkeleton.vue";
import ListVerticalCardsSkeleton from "@/views/ListVerticalCardsSkeleton.vue";

const props = defineProps({
  userId: String,
})

const currentUser = ref(getCurrentUser());
const apiUrl = inject('apiUrl')
const socialUsers = ref([]);
const loading = ref(false);
const hasMoreFriends = ref(true);

const PAGE_SIZE = 9;
let currentPage = 0;

async function loadFriends() {
  try {
    loading.value = true;
    const response = await axios.get(
        `${apiUrl}friend/friendsOf/${props.userId}`,
        {
          headers: {
            userId: currentUser.value.id,
          },
          params: {
            page: currentPage,
            size: PAGE_SIZE,
          },
        }
    );

    const newFriends = response.data.response.content;

    // Add the new cards
    socialUsers.value = socialUsers.value.concat(newFriends);

    // Check if there are more friends to load
    if (newFriends.length < PAGE_SIZE) {
      hasMoreFriends.value = false;
    }

    console.log('Response Friends: ', response.data);
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
}

async function cargarMasSugerencias() {
  currentPage++;
  await loadFriends();
}
</script>

<template>
  <InfinityScroll
      class="flex flex-wrap justify-around space-x4"
      :hasMoreElements="hasMoreFriends"
      @LoadMoreElements="cargarMasSugerencias">
    <SocialUserVerticalCard
        v-for="user in socialUsers"
        :user="user"
        :request=true
        :key="user.id"
        class=""
    />
  </InfinityScroll>
  <ListVerticalCardsSkeleton v-if="loading" />
</template>