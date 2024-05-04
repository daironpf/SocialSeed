<script setup>
import {inject, ref} from "vue";
import axios from "axios";
import BaseTooltip from "@/views/utils/BaseTooltip.vue";

// Props
const props = defineProps({
  userIdRequest: String,
  userIdTarget: String
})

// Hover state
const hover = ref({follow:false});

// Injected dependency
const apiUrl = inject('apiUrl');

// Emits
const emit = defineEmits(['updateStatusFollow'])

// Function to unfollow a user
async function unFollowUser() {
  try {
    const response = await axios.post(
        `${apiUrl}follow/unfollow/${props.userIdTarget}`,
        null, // No data in the body
        {
          headers: {
            userId: props.userIdRequest
          }
        }
    );

    if (response.status === 200) {
      // update the isFollow status
      emit('updateStatusFollow', false);
    }

    console.log(response.data); // You can handle the response as you wish
  } catch (error) {
    console.error('Error in following user:', error);
    // You can handle the error as you wish, e.g., show a notification to the user
  }
}
</script>

<template>
  <!--  unFollow Button-->
    <BaseTooltip
        :content="$t('unfollowMessage')"
        placement="bottom">
          <button
              @click="unFollowUser()"
              @mouseover="hover.follow = true"
              @mouseout="hover.follow = false"
              class="button-vertical bg-white text-black
                hover:bg-red-100 hover:text-red-600 hover:border-red-400
                focus:outline-none focus:shadow-outline">
            <span v-if="!hover.follow">
              {{ $t('following') }}
            </span>
            <span v-else>
              <fa icon="fa-solid fa-ban" class="text-red-600 mr-1"/>
              {{ $t('follow') }}
            </span>
          </button>
    </BaseTooltip>

</template>

<style scoped>

</style>