<script setup>
import {ref} from "vue";
import BaseTooltip from "@/views/utils/BaseTooltip.vue";
import FriendService from "@/services/friend-service.js";

// Props
const props = defineProps({
  userIdTarget: String
})

// Hover state
const hover = ref({cancel:false});

// Emits
const emit = defineEmits(['updateStatusOfIsRequestFriendshipSending'])

// Function to request Friendship a user
async function manageRequestFriendshipRecived() {

}
</script>

<template>
  <BaseTooltip
      :content="$t('clickToShowManageOptions')"
      placement="bottom">
    <button
        @click="manageRequestFriendshipRecived()"
        @mouseover="hover.cancel = true"
        @mouseout="hover.cancel = false"
        class="button-vertical bg-white text-black mr-1
                hover:bg-red-100 hover:text-red-600 hover:border-red-400
                focus:outline-none focus:shadow-outline">
            <span v-if="!hover.cancel">
              {{ $t('request') }} <
            </span>
      <span v-else>
              ?? {{ $t('request') }} ??
            </span>
    </button>
  </BaseTooltip>
</template>

<style scoped>

</style>