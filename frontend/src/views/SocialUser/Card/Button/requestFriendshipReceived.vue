<script setup>
import {ref} from "vue";
import BaseTooltip from "@/views/utils/BaseTooltip.vue";
import FriendService from "@/services/friend-service.js";
import FriendRequestDecisionModalView from "@/views/SocialUser/Card/Button/ModalDialogs/FriendRequestDecisionModalView.vue";

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
// @click="manageRequestFriendshipRecived()"
}

const modalActive = ref(null);
const toggleModal = () => {
  modalActive.value = !modalActive.value;
};
</script>

<template>
  <BaseTooltip
      :content="$t('clickToShowManageOptions')"
      placement="bottom">
    <button
        @click="toggleModal"
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
  <FriendRequestDecisionModalView
      :modalActive="modalActive"
      @close-modal="toggleModal"
  />
</template>

<style scoped>

</style>