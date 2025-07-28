<script setup>
import {ref} from "vue";
import BaseTooltip from "@/components/BaseTooltip.vue";
import FriendRequestDecisionModalView from "@/features/UserProfile/components/card/button/modalDialogs/FriendRequestDecisionModalView.vue";

// Props
const props = defineProps({
  userIdTarget: String
})

// Hover state
const hover = ref({cancel:false});

// Emits
const emit = defineEmits(['updateRequestReceived','updateStatusOfIsFriend'])

// Function to request Friendship a user
async function updateStatusOfIsRequestFriendshipReceived() {
  emit('updateRequestReceived')
}

async function updateStatusOfIsFriend(){
  emit('updateStatusOfIsFriend')
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
      :userIdTarget="userIdTarget"
      :modalActive="modalActive"
      @close-modal="toggleModal"
      @updateStatusOfIsFriend="updateStatusOfIsFriend"
      @updateRequestReceived="updateStatusOfIsRequestFriendshipReceived"
  />
</template>

<style scoped>

</style>