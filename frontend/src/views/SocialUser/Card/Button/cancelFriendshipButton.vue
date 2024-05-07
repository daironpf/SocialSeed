<script setup>
import {ref} from "vue";
import FriendService from "@/services/friend-service.js";
import ModalWithAcceptAndCancelButton from "@/views/utils/ModalWindow/ModalWithAcceptAndCancelButton.vue";
import { TypeOfModals } from "@/libs/constants/TypeOfModals.js";

// Props
const props = defineProps({
  userIdTarget: String
})

// Hover state
const hover = ref({follow:false});

// Emits
const emit = defineEmits(['updateStatusOfIsFriend'])

// Function to cancel Friendship
async function cancelFriendship(){
  console.log('Here cancel the friendship relationship between users');

  // FriendService.cancelFriendship(props.userIdTarget)
  //     .then(data => {
  //       console.log('Se eliminó la amistad entre los usuarios:', data);
  //       emit('updateStatusOfIsFriend');
  //     })
  //     .catch(error => {
  //       console.error('Error in drop friendship:', error);
  //     });

}

const modalActive = ref(false);
const modalType = ref('');

modalType.value = TypeOfModals.Warning;
const toggleModal = () => {
  modalActive.value = !modalActive.value;
};
</script>

<template>
  <!--  cancelFriendship Button-->
  <button
      @click="toggleModal"
      @mouseover="hover.friend = true"
      @mouseout="hover.friend = false"
      class="button-vertical bg-white text-black mr-1
            hover:bg-red-100 hover:text-red-600 hover:border-red-400
            focus:outline-none focus:shadow-outline">
    <fa v-if="hover.friend" icon="fa-solid fa-ban" class="text-red-600"/>
    {{ hover.friend ? $t('friendship') : $t('friends') }}
  </button>

  <ModalWithAcceptAndCancelButton
      :modalActive="modalActive"
      :title="$t('clickToShowManageOptions')"
      :message="$t('blockUserToPreventFriendRequest')"
      :modalType="modalType"
      @close-modal="toggleModal"
      @accept-button="cancelFriendship"
      @cancel-button="toggleModal"
  />
</template>

<style scoped>

</style>