<script setup>
import {ref} from "vue";
import FriendService from "@/core/a mover/friend-service.ts";
import WarningModalWindowView from "@/components/ModalWindow/WarningModalWindowView.vue";

// Props
const props = defineProps({
  userIdTarget: String,
  fullName: String
})

// Hover state
const hover = ref({follow:false});

// Emits
const emit = defineEmits(['updateStatusOfIsFriend'])

// Function to cancel Friendship
async function cancelFriendship(){
  FriendService.cancelFriendship(props.userIdTarget)
      .then(data => {
        console.log('Cancel the friendship relationship between users:', data);
        emit('updateStatusOfIsFriend');
      })
      .catch(error => {
        console.error('Error in cancel friendship:', error);
      });
  toggleModal()
}

const modalActive = ref(false);

const toggleModal = () => {
  modalActive.value = !modalActive.value;
};
</script>

<template>
  <!--  cancelFriendship button-->
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

  <WarningModalWindowView
      :modalActive="modalActive"
      :title="$t('confirmCancelFriendship')"
      :message="$t('confirmCancelFriendshipMessage') + props.fullName+' ?'"
      :acceptBtnText="$t('proceed')"
      :cancelBtnText="$t('cancel')"
      @close-modal="toggleModal"
      @accept-button="cancelFriendship"
      @cancel-button="toggleModal"
  />
</template>

<style scoped>

</style>