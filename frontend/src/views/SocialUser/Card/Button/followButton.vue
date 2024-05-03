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
const emit = defineEmits(['updateStatusFollow'])

// Function to follow a user
async function followUser() {
  try {
    const response = await axios.post(
        `${apiUrl}follow/follow/${props.userIdTarget}`,
        null, // No data in the body
        {
          headers: {
            userId: props.userIdRequest
          }
        }
    );

    if (response.status === 200) {
      // update the isFollow status
      emit('updateStatusFollow', true);
    }
    console.log(response.data);
  } catch (error) {
    console.error('Error in following user:', error);
  }
}

</script>

<template>
  <!--  Follow Button-->
  <button
      @click="followUser()"
      class="bg-blue-300 text-sm font-bold
            text-white p-2 rounded-lg w-28 focus:outline-none focus:shadow-outline
            hover:bg-blue-500
            ">
    <fa icon="fa-solid fa-person-circle-plus" class="text-white-600"/>
    Seguir
  </button>
</template>

<style scoped>

</style>