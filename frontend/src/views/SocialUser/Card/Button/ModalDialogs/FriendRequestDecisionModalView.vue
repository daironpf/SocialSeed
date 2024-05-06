<script setup>
import BaseTooltip from "@/views/utils/BaseTooltip.vue";
import FriendService from "@/services/friend-service.js";
import {data} from "autoprefixer";

const props = defineProps({
  modalActive: {
    type: Boolean,
    default: false,
  },
  userIdTarget: String
})

// Emits
const emit =defineEmits(['close-modal','update-request-received']);

// Function to Cancel Received Request Friendship
async function cancelReceivedRequest() {
  FriendService.cancelReceivedRequestFriendship(props.userIdTarget)
      .then(data => {
        console.log('Cancel the Received Request Friendship successful:', data);
        emit('close-modal');
        emit('update-request-received');
      })
      .catch(error => {
        console.error('Error in Send Request Friendship:', error);
      })
}
</script>

<template>
  <Teleport to="body">
    <Transition name="modal-outer">
      <div
          v-show="modalActive"
          class="absolute w-full bg-black bg-opacity-30 h-screen top-0 left-0 flex justify-center px-8">
        <Transition name="modal-inner">
          <div
              v-if="modalActive"
              class="pt-2 pb-2 pl-1 pr-1 rounded bg-white self-start mt-32 max-w-screen-md">

            <!-- Header-->
            <div class="flex items-center justify-between border-b border-gray-300 pb-2 pl-4">
              <!-- Title -->
              <div class="flex items-center">
                <h2 class="font-bold">
                  {{ $t('friend_request_decision_modal_title') }}
                </h2>
              </div>
              <!-- Close Button-->
              <button @click="$emit('close-modal')"
                      class="text-gray-600
                    hover:text-gray-800
                    focus:outline-none
                    focus:border rounded-full p-3 hover:bg-gray-200">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <!-- Body-->
            <div>
                <h1 class="text-black font-semibold"></h1>
                <p class="font-semibold m-3 mt-6 ">
                  {{ $t('friend_request_decision_modal_message')}}
                </p>
            </div>

            <!-- Buttons Section-->
            <div class="mt-4 flex justify-center">
              <BaseTooltip
                  :content="$t('clickToBecomeFriends')"
                  placement="bottom">
                <button
                    class="button-vertical bg-blue-300 mr-1 text-white
                  hover:bg-blue-500
                  focus:outline-none focus:shadow-outline">
                  {{ $t('acceptRequest')}}
                </button>
              </BaseTooltip>

              <BaseTooltip
                :content="$t('cancelFriendRequest')"
                placement="bottom">
                <button
                    @click="cancelReceivedRequest()"
                    class="button-vertical bg-white text-red-500
                  hover:bg-red-100 hover:text-red-600 hover:border-red-400
                  focus:outline-none focus:shadow-outline">
                  <fa icon="fa-solid fa-ban" class="text-red-600 mr-1"/>
                  {{ $t('request')}}
                </button>
              </BaseTooltip>

              <BaseTooltip
                  :content="$t('blockUserToPreventFriendRequest')"
                  placement="bottom">
                <button
                    class="button-vertical bg-white text-red-400 ml-1
                  hover:bg-red-200 hover:text-orange-700 hover:border-red-300
                  focus:outline-none focus:shadow-outline">
                <fa icon="fa-solid fa-person-circle-xmark"
                    class="hover:cursor-pointer
                           focus:outline-none focus:shadow-outline
                           text-md text-red-600 w-5 h-4
                            "/>
                  {{ $t('block')}}
                </button>
              </BaseTooltip>
            </div>

          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.modal-outer-enter-active,
.modal-outer-leave-active {
  transition: opacity 0.3s cubic-bezier(0.52, 0.02, 0.19, 1.02);
}

.modal-outer-enter-from,
.modal-outer-leave-to {
  opacity: 0;
}

.modal-inner-enter-active{
  transition: all 0.3s cubic-bezier(0.52, 0.02, 0.19, 1.02) 0.15s;
}
.modal-inner-leave-active {
  transition: all 0.3s cubic-bezier(0.52, 0.02, 0.19, 1.02);
}
.modal-inner-enter-from{
  opacity: 0;
  transform: scale(0.8);
}
.modal-inner-leave-to {
  transform: scale(0.8);
}
</style>