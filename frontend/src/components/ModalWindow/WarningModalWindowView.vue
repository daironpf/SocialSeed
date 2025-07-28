<script setup lang="ts">
import {FontAwesomeIcon as Fa} from "@fortawesome/vue-fontawesome";

interface ModalWindowProps {
  modalActive: Boolean
  title: String;
  message: String;
  cancelBtnText?: string; // Propiedad opcional
  acceptBtnText?: string; // Propiedad opcional
}

// Definición de las props utilizando la interfaz
const props = defineProps<ModalWindowProps>();

// Definición de los eventos emitidos
const emit =defineEmits(['close-modal','accept-button','cancel-button']);

// Función que se llama al hacer clic en el botón de cancelar
async function cancelButtonRequest() {
  console.log('Click in Cancel-button was Received');
  emit('cancel-button');
}

// Función que se llama al hacer clic en el botón de aceptar
async function acceptButtonRequest() {
  console.log('Click en Accept-button recibido');
  emit('accept-button');
}
</script>

<template>
  <Teleport to="body">
    <Transition name="modal-outer">
      <div
          v-show="props.modalActive"
          class="absolute w-full bg-black bg-opacity-30 h-screen top-0 left-0 flex justify-center px-8"
      >
        <Transition name="modal-inner">
          <div
              v-if="props.modalActive"
              class="p-0 rounded-lg bg-white self-start mt-32 max-w-screen-md"
          >
            <!-- Header -->
            <div class="flex items-center justify-between rounded-t-lg border-b border-gray-300 pt-2 pb-2 pl-4 pr-3 bg-red-500">
              <!-- Title -->
              <div class="flex items-center">
                <div>
                  <Fa icon="fa-solid fa-triangle-exclamation" class="text-white w-8 h-8 mr-4"/>
                </div>
                <h2 class="font-bold text-lg text-white">
                  {{ props.title }}
                </h2>
              </div>
              <!-- Close button -->
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

            <!-- Body -->
            <div class="mb-7 mt-7 ml-3 mr-3 text-gray-600 font-bold">
              <p>
                {{ props.message }}
              </p>
            </div>

            <!-- Buttons Section -->
            <div class="mt-4 flex rounded-b-md justify-end p-4 mb-0 bg-gray-300">
              <button
                  @click="cancelButtonRequest"
                  class="button-vertical bg-white text-black
                hover:bg-white-100 hover:text-black hover:border-gray-500
                focus:outline-none focus:shadow-outline"
              >
                {{ props.cancelBtnText }}
              </button>

              <button
                  @click="acceptButtonRequest"
                  class="button-vertical bg-red-300 ml-1 text-white border-red-300
                hover:bg-red-500 hover:border-red-500
                focus:outline-none focus:shadow-outline"
              >
                {{ props.acceptBtnText }}
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