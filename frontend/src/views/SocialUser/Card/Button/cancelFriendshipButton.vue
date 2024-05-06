<script setup>
import {ref} from "vue";
import FriendService from "@/services/friend-service.js";

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
  FriendService.cancelFriendship(props.userIdTarget)
      .then(data => {
        console.log('Se eliminó la amistad entre los usuarios:', data);
        emit('updateStatusOfIsFriend');
      })
      .catch(error => {
        console.error('Error in drop friendship:', error);
      });

}

</script>

<template>
  <!--  cancelFriendship Button-->
  <button
      @click="cancelFriendship()"
      @mouseover="hover.friend = true"
      @mouseout="hover.friend = false"
      class="button-vertical bg-white text-black mr-1
            hover:bg-red-100 hover:text-red-600 hover:border-red-400
            focus:outline-none focus:shadow-outline">
    <fa v-if="hover.friend" icon="fa-solid fa-ban" class="text-red-600"/>
    {{ hover.friend ? $t('friendship') : $t('friends') }}
  </button>
</template>

<style scoped>

</style>