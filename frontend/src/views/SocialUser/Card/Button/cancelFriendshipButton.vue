<script setup>
import {inject, ref} from "vue";


// Props
const props = defineProps({
  userIdTarget: String
})

// Hover state
const hover = ref({follow:false});

// Injected dependency
const apiUrl = inject('apiUrl');

// Emits
const emit = defineEmits(['updateStatusFollow'])

// Function to cancel Friendship
async function cancelFriendship(){

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
    {{ hover.friend ? 'Amistad' : 'Son Amigos' }}
  </button>
</template>

<style scoped>

</style>