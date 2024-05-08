<script setup>
import BaseTooltip from "@/views/utils/BaseTooltip.vue";
import {FontAwesomeIcon as Fa} from "@fortawesome/vue-fontawesome";
import {TypeOfModals} from "@/libs/constants/TypeOfModals.js";

const props = defineProps({
  modalActive: {
    type: Boolean,
    default: false,
  },
  title:{
    type: String,
    default: "Title Here"
  },
  message:{
    type: String,
    default: "Message to user was Here"
  },
  modalType:{
    type: String
  }
})

// Emits
const emit =defineEmits(['close-modal','accept-button','cancel-button']);

// This function will be called when user click in Cancel Button
async function cancelButtonRequest() {
  console.log('Click in Cancel-Button was Received');
  emit('cancel-button');
}

// This function will be called when user click in Accept Button
async function acceptButtonRequest() {
  console.log('Click in Accept-Button was Received');
  emit('accept-button');
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
              class="p-0 rounded-lg bg-white self-start mt-32 max-w-screen-md">

            <!-- Header-->
            <div class="flex items-center justify-between rounded-t-lg border-b border-gray-300 pt-2 pb-2 pl-4 pr-3"
                  :class="{'bg-red-500': modalType === TypeOfModals.WARNING}">
              <!-- Title -->
              <div class="flex items-center">
                <div v-if="modalType == TypeOfModals.WARNING">
                  <fa icon="fa-solid fa-triangle-exclamation" class="text-white w-8 h-8 mr-4"/>
                </div>
                <h2 class="font-bold text-lg text-white">
                  {{ title }}
                </h2>

              </div>
              <!-- Close Button-->
              <button @click="$emit('close-modal')"
                      class="text-white
                    hover:text-white
                    focus:outline-none
                    focus:border rounded-full p-1 hover:bg-red-400">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <!-- Body-->
            <div class="mb-7 mt-7 ml-3 mr-3 text-gray-600 font-bold">
              <p>
                {{ message }}
              </p>
            </div>

            <!-- Buttons Section-->
            <div class="mt-4 flex rounded-b-md justify-end p-4 mb-0 bg-gray-300">
              <button
                  @click="cancelButtonRequest"
                  class="button-vertical bg-white text-red-500
                  hover:bg-red-100 hover:text-red-600 hover:border-red-400
                  focus:outline-none focus:shadow-outline">
                <fa icon="fa-solid fa-ban" class="text-red-600 mr-1"/>
                Cancel
              </button>

              <button
                    @click="acceptButtonRequest"
                    class="button-vertical bg-blue-300 ml-1 text-white border-blue-300
                  hover:bg-blue-500 hover:border-blue-500
                  focus:outline-none focus:shadow-outline">
                  Accept
              </button>
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