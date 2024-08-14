<script setup>
import {inject, ref} from "vue";
import FollowButton from "@/features/UserProfile/components/card/button/followButton.vue";
import unFollowButton from "@/features/UserProfile/components/card/button/unFollowButton.vue";
import sendRequestFriendshipButton from "@/features/UserProfile/components/card/button/sendRequestFriendshipButton.vue";
import cancelRequestFriendshipButton from "@/features/UserProfile/components/card/button/cancelRequestFriendshipButton.vue";
import cancelFriendshipButton from "@/features/UserProfile/components/card/button/cancelFriendshipButton.vue";
import RequestFriendshipReceived from "@/features/UserProfile/components/card/button/requestFriendshipReceived.vue";

// Props
const props = defineProps({
  user: Object,
  request: Boolean
})

// Transform user to Reactive
const socialUser = ref(props.user);

// url to load images
const imgUrl = inject('imgUrl');

function updateStatusFollow(status){
  socialUser.value.isFollow = status;
}

function updateStatusOfIsRequestFriendshipSending(status){
  socialUser.value.isRequestFriendshipSending = status;
}

function toggleStatusOfIsRequestFriendshipReceived(){
  socialUser.value.isRequestFriendshipReceived = !socialUser.value.isRequestFriendshipReceived;
}

function toggleStatusOfIsFriend(){
  socialUser.value.isFriend = !socialUser.value.isFriend;
}


</script>

<template>
  <div class="bg-gray-50 rounded-lg shadow-md w-64 pl-3 pr-3 pt-4 pb-3 item-center m-1">
    <!-- Profile picture -->
    <div class="flex items-center flex-col">
      <!-- User data -->
      <div class="h-44 flex items-center flex-col">
        <img :src="imgUrl + socialUser.profileImage"
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
            :userIdTarget = "socialUser.id"
        />

        <cancelFriendshipButton
            v-if="socialUser.isFriend"
            @updateStatusOfIsFriend="toggleStatusOfIsFriend"
            :userIdTarget = "socialUser.id"
            :full-name = "socialUser.fullName"
        />

        <cancel-request-friendship-button
            v-if="socialUser.isRequestFriendshipSending"
            @updateStatusOfIsRequestFriendshipSending="updateStatusOfIsRequestFriendshipSending"
            :userIdTarget = "socialUser.id"
        />

        <RequestFriendshipReceived
            v-if="socialUser.isRequestFriendshipReceived"
            @updateStatusOfIsFriend="toggleStatusOfIsFriend"
            @updateRequestReceived="toggleStatusOfIsRequestFriendshipReceived"
            :userIdTarget = "socialUser.id"
        />


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