<script setup>
import {inject, ref, defineProps, defineEmits} from "vue";
import axios from "axios";

// Props
const props = defineProps({
  user: Object,
  request: Boolean
})

const emits = defineEmits(['userFollowed']);

// LocalStorage
const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));

// Injected dependency
const apiUrl = inject('apiUrl');

// Hover state
const hover = ref({follow:false,friend:false});

// Function to follow a user
async function followUser(userId) {
  try {
    const response = await axios.post(
        `${apiUrl}follow/follow/${props.user.id}`,
        null, // No data in the body
        {
          headers: {
            userId: currentUser.value.id
          }
        }
    );

    if (response.status === 200) {
      props.user.isFollow = true;
      emits('userFollowed', 'Usuario Seguido Exitosamente');
    }

    console.log(response.data); // You can handle the response as you wish
  } catch (error) {
    console.error('Error in following user:', error);
    // You can handle the error as you wish, e.g., show a notification to the user
  }
}

// Function to handle user authentication
function getCurrentUser() {
  return JSON.parse(localStorage.getItem('currentUser'));
}

</script>

<template>


  <div class="bg-gray-50 rounded-lg shadow-md w-64 pl-3 pr-3 pt-4 pb-3 item-center m-1">
    <!-- Profile picture -->
    <div class="flex items-center flex-col">
      <!-- User data -->
      <div class="h-44 flex items-center flex-col">
        <img :src="user.profileImage"
             alt="Foto de usuario"
             class="w-16 h-16 rounded-full mr-4 mb-2">
        <div>
          <router-link :to="{ name: 'su-profile', params: { id: user.id }}">
            <h2 class="text-md font-semibold mb-1">{{user.fullName}}</h2>
            <p class="text-sm text-gray-600 ">@{{user.userName}}</p>
          </router-link>
          <span
              v-if="user.mutualFriends"
              class="text-slate-700 dark:text-slate-500" >
            <span class="font-semibold">
              {{user.mutualFriends}}
            </span> amigos en común
          </span>
          <div
              v-if="user.isFollower"
              class="pt-1 ">
            <span>
              <span class="text-black-100 pt-1 pb-1 text-sm font-semibold">Te sigue</span>
            </span>
          </div>
        </div>
      </div>

      <!-- Options Buttons -->
      <div class="mt-2 mb-1" v-if="props.request">
        <!-- Botón de Pedir Amistad -->
        <button
            v-if="!user.isFriend"
            class="bg-blue-300 text-sm font-bold mr-1
            text-white p-2 rounded-lg w-28 focus:outline-none focus:shadow-outline
            hover:bg-blue-500">
          <fa icon="fa-solid fa-user-plus" class="text-white-600"/>
          Amistad
        </button>
        <button
            v-if="user.isFriend"
            @mouseover="hover.friend = true"
            @mouseout="hover.friend = false"
          class="text-sm font-bold bg-white text-black border p-2 mr-1 rounded-lg w-28 h-15
          focus:outline-none focus:shadow-outline
          hover:bg-red-100 hover:text-red-600 hover:border-red-400
          ">
          <fa v-if="hover.friend" icon="fa-solid fa-ban" class="text-red-600"/>
          {{ hover.friend ? ' Amistad' : 'Amigos' }}
        </button>

        <!-- Botón de Seguir -->
        <button
            v-if="!user.isFollow"
            @click="followUser(user.id)"
            class="bg-blue-300 text-sm font-bold
            text-white p-2 rounded-lg w-28 focus:outline-none focus:shadow-outline
            hover:bg-blue-500
            ">
          <fa icon="fa-solid fa-person-circle-plus" class="text-white-600"/>
          Seguir
        </button>
        <button
            v-if="user.isFollow"
            @mouseover="hover.follow = true"
            @mouseout="hover.follow = false"
            class="text-sm font-bold bg-white text-black border
            hover:bg-red-100 hover:text-red-600 hover:border-red-400
            p-2 rounded-lg w-28 focus:outline-none focus:shadow-outline">
          <fa v-if="hover.follow" icon="fa-solid fa-ban" class="text-red-600"/>
          {{ hover.follow ? 'Seguir' : 'Siguiendo' }}
        </button>

      </div>
    </div>
  </div>
</template>

<style scoped>

</style>