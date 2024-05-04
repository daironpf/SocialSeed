<script setup>
import {inject} from "vue";
import axios from "axios";

// Props
const props = defineProps({
  userIdRequest: String,
  userIdTarget: String
})

// Injected dependency
const apiUrl = inject('apiUrl');

// Emits
const emit = defineEmits(['updateStatusOfIsRequestFriendshipSending'])

// Function to request Friendship a user
async function requestFriendship() {
  try {
    const response = await axios.post(
        `${apiUrl}friend/createRequest/${props.userIdTarget}`,
        null, // No data in the body
        {
          headers: {
            userId: props.userIdRequest
          }
        }
    );

    if (response.status === 200) {
      // update the IsRequestFriendshipSending status
      emit('updateStatusOfIsRequestFriendshipSending', true);
    }
    console.log(response.data);
  } catch (error) {
    console.error('Error in request friendship to user:', error);
  }
}
</script>

<template>
  <button
      @click="requestFriendship()"
      class="button-vertical bg-blue-300 mr-1 text-white
            hover:bg-blue-500
            focus:outline-none focus:shadow-outline">
    <fa icon="fa-solid fa-user-plus" class="text-white-600"/>
    Amistad
  </button>
</template>

<style scoped>

</style>