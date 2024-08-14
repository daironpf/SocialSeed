<script setup>
import BaseTooltip from "@/components/BaseTooltip.vue";
import FriendService from "@/core/a mover/friend-service.ts";

// Props
const props = defineProps({
  userIdTarget: String
})

// Emits
const emit = defineEmits(['updateStatusOfIsRequestFriendshipSending'])

// Function to request Friendship a user
async function requestFriendship() {
  FriendService.sendRequestFriendship(props.userIdTarget)
      .then(data => {
        console.log('Send Request Friendship successful:', data);
        emit('updateStatusOfIsRequestFriendshipSending', true);
      })
      .catch(error => {
        console.error('Error in Send Request Friendship:', error);
      })
}
</script>

<template>
  <BaseTooltip
      :content="$t('sendFriendRequest')"
      placement="bottom">
        <button
            @click="requestFriendship()"
            class="button-vertical bg-blue-300 mr-1 text-white
                  hover:bg-blue-500
                  focus:outline-none focus:shadow-outline">
          <fa icon="fa-solid fa-user-plus" class="text-white-600"/>
          {{ $t('friend') }}
        </button>
  </BaseTooltip>
</template>

<style scoped>

</style>