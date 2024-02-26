<script setup>
import {ref} from "vue";
import axios from 'axios';

const nombre = ref('');
const userName = ref(null);
const email = ref('');

async function agregarNuevoUsuario(){

  const nuevoUsuario = {
    fullName: nombre.value,
    userName: userName.value,
    email: email.value
  };

  try {
    // Realiza una petición POST a la URL especificada
    let url = 'http://127.0.0.1:8081/api/v0.0.1/socialUser/createSocialUser';

    const response = await axios.post(url, nuevoUsuario);
    console.log('Respuesta del servidor:', response.data);

    // Limpia el formulario después de agregar el usuario
    nombre.value = '';
    userName.value = null;
    email.value = '';

    if (response.status === 201){
      this.$router.push({ name: 'home' });
    }
  } catch (error) {
    console.error('Error al agregar usuario:', error);
  }
}
</script>

<template>
  <div class="flex flex-col items-center justify-center h-screen">
    <h2 class="text-3xl font-semibold mb-6">Bienvenido a la Red Social</h2>
    <form @submit.prevent="crearUsuario" class="space-y-4">
      <div class="flex flex-col">
        <label for="nombre" class="text-gray-600">Nombre completo:</label>
        <input type="text" id="nombre" v-model="nombre" required class="w-full rounded border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-200 px-4 py-2">
      </div>
      <div class="flex flex-col">
        <label for="userName" class="text-gray-600">Nombre de usuario:</label>
        <input type="text" id="userName" v-model="userName" required class="w-full rounded border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-200 px-4 py-2">
      </div>
      <div class="flex flex-col">
        <label for="email" class="text-gray-600">Email:</label>
        <input type="email" id="email" v-model="email" required class="w-full rounded border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-200 px-4 py-2">
      </div>
      <button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded-full transition-colors">Crear usuario</button>
    </form>
  </div>
</template>


<style scoped>

</style>