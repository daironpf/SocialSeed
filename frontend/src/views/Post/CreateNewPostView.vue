<template>
  <div class="bg-gray-50 rounded-lg shadow m-1 ml-10 mr-10 p-2">
    <div class="max-w-lg mx-auto flex items-center border-b border-gray-300 pb-4 mb-4">
      <img :src="currentUser.profileImage" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
      <button @click="showDialog" class="flex-1 px-4 py-2 bg-gray-50 text-gray-600 rounded border border-gray-400 shadow-md hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
        Crear publicación
      </button>

      <div v-if="isDialogOpen"
           class="fixed left-0 top-0 bg-black bg-opacity-50 w-screen h-screen flex justify-center items-center"
           @click="hideDialog">
        <div class="bg-white rounded shadow-md w-[40%] h-[50%] flex flex-col" @click.stop>

          <!-- Encabezado -->
          <div class="flex items-center justify-between p-4 border-b border-gray-300">
            <div class="flex items-center">
              <img :src="currentUser.profileImage" alt="Foto de usuario" class="w-8 h-8 rounded-full mr-2">
              <span class="text-gray-700 font-semibold">{{ currentUser.fullName }}</span>
            </div>
            <button @click="hideDialog"
                    class="text-gray-600
                    hover:text-gray-800
                    focus:outline-none
                    focus:border rounded-full p-3 hover:bg-gray-200">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <!-- Contenido -->
          <div class="p-4 flex-grow">
            <textarea
                v-model="postContent"
                class="w-full h-full rounded-md p-2"
                placeholder="Escribe tu publicación aquí..."
                style="resize: none; border: none;"
                @click.stop>
            </textarea>
          </div>

          <!-- Barra de Botones-->
          <div class="flex justify-between bg-gray-100 border">

            <!-- Botones Izquierdos-->
            <div class="pl-4 flex gap-3">
              <div class="flex items-center">
                <div class="h-12 w-12 bg-gray-200 rounded rounded-full flex items-center justify-center
                hover:bg-gray-300 focus:outline-none focus:bg-gray-100 cursor-pointer">
                  <fa icon=" fa-solid fa-image" class="text-blue-400 text-xl"/>
                </div>
              </div>
              <div class="flex items-center">
                <div class="h-12 w-12 bg-gray-200 rounded rounded-full flex items-center justify-center
                hover:bg-gray-300 focus:outline-none focus:bg-gray-100 cursor-pointer">
                  <fa icon="fa-solid fa-clock" class="text-purple-500 text-xl"/>
                </div>
              </div>
            </div>

            <!-- Botones Derechos-->
            <div class="p-3 px-6 flex gap-4">
              <!-- Botón de visibilidad -->
              <button
                  @click="showModal = true"
                  class="bg-white rounded-full px-4 py-2 flex items-center space-x-2 text-black cursor-pointer hover:bg-gray-100">
                <fa :icon="selectedOption.icon" class="text-gray-600"></fa>
                <span class="text-sm">{{ selectedOption.label }}</span>
                <fa icon="fa-solid fa-sort-down" class="text-gray-600"></fa>
              </button>

              <button
                  @click="publishPost"
                  class="bg-blue-400 rounded px-4 py-2 text-white cursor-pointer hover:bg-blue-600">
                Publicar
              </button>
            </div>

          </div>

        </div>
      </div>
    </div>

    <!-- Botones para personalizar el Post -->
    <div class="max-w-lg mx-auto">
      <div class="flex justify-between">
        <div class="flex items-center mb-4">
          <div class="p-2 w-full rounded-lg flex items-center justify-center hover:bg-gray-200 focus:outline-none focus:bg-gray-100 cursor-pointer">
            <!-- Icono e texto -->
            <fa icon="fa-solid fa-image" class="text-blue-400"/>
            <p class="pl-2 text-gray-500"> Imagen </p>
          </div>
        </div>
        <div class="flex items-center mb-4">
          <div class="p-2 w-full rounded-lg flex items-center justify-center hover:bg-gray-200 focus:outline-none focus:bg-gray-100 cursor-pointer">
            <!-- Icono e texto -->
            <fa icon="fa-solid fa-pencil" class="text-yellow-600"/>
            <p @click="showDialog" class="pl-2 text-gray-500">Escribir Post</p>
          </div>
        </div>
        <div class="flex items-center mb-4">
          <div class="p-2 w-full rounded-lg flex items-center justify-center hover:bg-gray-200 focus:outline-none focus:bg-gray-100 cursor-pointer">
            <!-- Icono e texto -->
            <fa icon="fa-solid fa-clock" class="text-purple-500"/>
            <p class="pl-2 text-gray-500">Programar Post</p>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Ventana emergente de notificación -->
  <div v-if="notificationVisible" class="fixed inset-x-0 bottom-0 z-50 flex justify-center mb-4">
    <div class="bg-green-400 text-white p-4 rounded-md shadow-md">
      {{ notificationMessage }}
    </div>
  </div>

  <!-- Ventana modal para visibilidad-->
  <div v-if="showModal" class="fixed inset-0 flex items-center justify-center z-50 bg-gray-900 bg-opacity-50">
    <div class="bg-white rounded-lg p-6 w-80">
      <h2 class="text-lg font-semibold mb-4 text-center">Seleccionar Visibilidad</h2>
      <div class="flex flex-col gap-2">
        <button v-for="(option, index) in options" :key="index" @click="selectVisibility(option)" class="bg-gray-100 hover:bg-gray-200 rounded-md flex items-center justify-center px-4 py-2">
          <fa :icon="option.icon" class="text-gray-600 pr-3"></fa>
          <span>{{ option.label }}</span>
        </button>
      </div>
      <button @click="showModal = false" class="mt-4 bg-gray-200 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-300 w-full">Cerrar</button>
    </div>
  </div>
</template>

<script setup>
import {inject, ref} from "vue";

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const isDialogOpen = ref(false);
const postContent = ref('');
const apiUrl = inject('apiUrl');
const notificationVisible = ref(false);
const notificationMessage = ref('');

function showDialog() {
  isDialogOpen.value = true;
}

function hideDialog() {
  isDialogOpen.value = false;
}

async function publishPost() {
  try {
    // Aquí supondré que tienes un endpoint /posts en tu API REST
    const response = await fetch(apiUrl+'post/createPost',
        {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                'userId': currentUser.value.id
              },
              body: JSON.stringify({
                content: postContent.value,
              }),
        });

    if (response.ok) {
      // Si la publicación es exitosa, puedes realizar alguna acción, como cerrar el modal o mostrar un mensaje de éxito
      hideDialog();
      showNotification('Publicación exitosa');
      console.log('Publicación exitosa');
    } else {
      // Si hay algún error en la publicación, puedes manejarlo aquí
      showNotification('Error al publicar');
      console.error('Error al publicar');
    }
  } catch (error) {
    // Manejo de errores de red u otros errores
    showNotification('Error al procesar la solicitud');
    console.error('Error al procesar la solicitud:', error);
  }
}

function showNotification(message) {
  notificationMessage.value = message;
  notificationVisible.value = true;
  setTimeout(() => {
    notificationVisible.value = false;
  }, 5000); // La notificación desaparecerá después de 5 segundos
}

// visibilidad
const showModal = ref(false);
const options = [
  { label: 'Público', value: 'public', icon: 'fa-solid fa-earth'},
  { label: 'Amigos', value: 'friends', icon: 'fa-solid fa-user-friends' },
  { label: 'Privado', value: 'private', icon: 'fa-solid fa-lock'},
  { label: 'Seguidores', value: 'followers', icon: 'fa-solid fa-users-line'},
  { label: 'Seguidores & Amigos', value: 'followers-plus-friends', icon: 'fa-solid fa-users-between-lines'}
];
const selectedOption = ref(options[0]);
const selectVisibility = (option) => {
  selectedOption.value = option;
  // Aquí puedes hacer algo con la opción seleccionada, como enviarla al servidor, etc.
  showModal.value = false;
};
</script>

<style scoped>

</style>
