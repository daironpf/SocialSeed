<script setup>
import {ref} from "vue";
import BaseTooltip from "@/components/BaseTooltip.vue";
import FriendService from "@/core/a mover/friend-service.ts";

// Props
const props = defineProps({
  userIdTarget: String
})

// Hover state
const hover = ref({cancel:false});

// Emits
const emit = defineEmits(['updateStatusOfIsRequestFriendshipSending'])

// Cancel Friend Resquest Sending
async function cancelFriendRequestSending() {
  FriendService.cancelSendRequestFriendship(props.userIdTarget)
      .then(data => {
        console.log('Cancel the Send Request Friendship successful:', data);
        emit('updateStatusOfIsRequestFriendshipSending', false);
      })
      .catch(error => {
        console.error('Error at the Cancel the Send Request Friendship:', error);
      });
}
</script>

<template>
  <BaseTooltip
      :content="$t('cancelFriendRequest')"
      placement="bottom">
    <button
        @click="cancelFriendRequestSending()"
        @mouseover="hover.cancel = true"
        @mouseout="hover.cancel = false"
        class="button-vertical bg-white text-black mr-1
                hover:bg-red-100 hover:text-red-600 hover:border-red-400
                focus:outline-none focus:shadow-outline">
            <span v-if="!hover.cancel">
              > {{ $t('request') }}
            </span>
      <span v-else>
              <fa icon="fa-solid fa-ban" class="text-red-600 mr-1"/>
              {{ $t('request') }}
            </span>
    </button>
  </BaseTooltip>
</template>
<style scoped>

</style>