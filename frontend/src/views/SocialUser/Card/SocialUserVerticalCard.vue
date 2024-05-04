<script setup>
import {inject, ref} from "vue";
import FollowButton from "@/views/SocialUser/Card/Button/followButton.vue";
import unFollowButton from "@/views/SocialUser/Card/Button/unFollowButton.vue";
import sendRequestFriendshipButton from "@/views/SocialUser/Card/Button/sendRequestFriendshipButton.vue";
import cancelRequestFriendshipButton from "@/views/SocialUser/Card/Button/cancelRequestFriendshipButton.vue";
import cancelFriendshipButton from "@/views/SocialUser/Card/Button/cancelFriendshipButton.vue";

// Props
const props = defineProps({
  user: Object,
  request: Boolean
})

// Transform user to Reactive
const socialUser = ref(props.user);

// LocalStorage
const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));

// url to load images
const s3Url = inject('s3Url');

// Hover state
const hover = ref({follow:false,friend:false});

// Function to handle user authentication
function getCurrentUser() {
  return JSON.parse(localStorage.getItem('currentUser'));
}

function updateStatusFollow(status){
  socialUser.value.isFollow = status;
}

function updateStatusOfIsRequestFriendshipSending(status){
  console.warn("before: isRequestFriendship: ",socialUser.value.isRequestFriendshipSending);
  socialUser.value.isRequestFriendshipSending = status;
  console.warn("after: isRequestFriendship: ",socialUser.value.isRequestFriendshipSending);
}

function updateStatusOfIsFriend(status){
  console.warn("before: isRequestFriendship: ",socialUser.value.isFriend);
  socialUser.value.isFriend = status;
  console.warn("after: isRequestFriendship: ",socialUser.value.isFriend);
}


</script>

<template>
  <div class="bg-gray-50 rounded-lg shadow-md w-64 pl-3 pr-3 pt-4 pb-3 item-center m-1">
    <!-- Profile picture -->
    <div class="flex items-center flex-col">
      <!-- User data -->
      <div class="h-44 flex items-center flex-col">
        <img :src="s3Url + socialUser.profileImage"
             alt="Foto de usuario"
             class="w-16 h-16 rounded-full mr-4 mb-2">
        <div>
          <router-link :to="{ name: 'su-profile', params: { id: socialUser.id }}">
            <h2 class="text-md font-semibold mb-1">{{socialUser.fullName}}</h2>
            <p class="text-sm text-gray-600 ">@{{socialUser.userName}}</p>
          </router-link>
          <span
              v-if="user.mutualFriends"
              class="text-slate-700 dark:text-slate-500" >
            <span class="font-semibold">
              {{socialUser.mutualFriends}}
            </span> amigos en común
          </span>
          <div
              v-if="socialUser.isFollower"
              class="pt-1 ">
            <span>
              <span class="text-black-100 pt-1 pb-1 text-sm font-semibold">Te sigue</span>
            </span>
          </div>
        </div>
      </div>

      <!-- Options Buttons -->
      <div class="mt-2 mb-1" v-if="props.request">
        <!-- Friend Buttons Sections-->
        <sendRequestFriendshipButton
            v-if="!socialUser.isFriend && !socialUser.isRequestFriendshipSending && !socialUser.isRequestFriendshipReceived"
            @updateStatusOfIsRequestFriendshipSending="updateStatusOfIsRequestFriendshipSending"
            :userIdRequest = "currentUser.id"
            :userIdTarget = "socialUser.id"
        />

        <cancelFriendshipButton
            v-if="socialUser.isFriend"
            @updateStatusOfIsFriend="updateStatusOfIsFriend"
            :userIdRequest = "currentUser.id"
            :userIdTarget = "socialUser.id"
        />


<!--        <button-->
<!--            v-if="socialUser.isFriend"-->
<!--            @mouseover="hover.friend = true"-->
<!--            @mouseout="hover.friend = false"-->
<!--          class="text-sm font-bold bg-white text-black border p-2 mr-1 rounded-lg w-28 h-15-->
<!--          focus:outline-none focus:shadow-outline-->
<!--          hover:bg-red-100 hover:text-red-600 hover:border-red-400-->
<!--          ">-->
<!--          <fa v-if="hover.friend" icon="fa-solid fa-ban" class="text-red-600"/>-->
<!--          {{ hover.friend ? ' Amistad' : 'Amigos' }}-->
<!--        </button>-->

        <!-- Follow Buttons Sections-->
        <follow-button
            v-if="!socialUser.isFollow"
            @updateStatusFollow="updateStatusFollow"
            :userIdTarget = "socialUser.id"
        />
        <unFollowButton
            v-if="socialUser.isFollow"
            @updateStatusFollow="updateStatusFollow"
            :userIdTarget = "socialUser.id"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>