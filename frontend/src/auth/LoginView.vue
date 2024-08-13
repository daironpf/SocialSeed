<script setup lang="ts">
import { inject, ref, Ref } from 'vue';
import { useRouter } from 'vue-router';

// Tipos de las variables
const email: Ref<string> = ref('');
const password: Ref<string> = ref('');
const error: Ref<string> = ref('');
const router = useRouter();

const apiUrl = inject<string>('apiUrl');
const currentUser: Ref<any | null> = ref(JSON.parse(localStorage.getItem('currentUser') ?? 'null'));

const login = async (): Promise<void> => {
  try {
    const response = await validateEmail(email.value, password.value);
    if (response && response.ok) {
      const userData = await response.json();
      localStorage.setItem('currentUser', JSON.stringify(userData.response)); // Almacena el usuario en localStorage
      currentUser.value = userData.response; // Actualiza la variable de usuario

      router.push({ name: 'feed' });

    } else {
      error.value = 'Invalid credentials, please try again.';
    }

  } catch (err: any) {
    console.error('Error logging in:', err);
    error.value = 'An error occurred while logging in. Please try again later.';
  }
};

const validateEmail = async (email: string, password: string): Promise<Response | undefined> => {
  try {
    if (!apiUrl) throw new Error('API URL is not provided');
    const response = await fetch(`${apiUrl}socialUser/getSocialUserByEmail/${email}`);
    return response;
  } catch (e) {
    console.error(e);
  }
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <form class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4" @submit.prevent="login">
      <h2 class="text-2xl mb-4 font-bold text-center">Iniciar Sesión</h2>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2" for="email">Correo Electrónico</label>
        <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="email" type="email" v-model="email" required>
      </div>
      <div class="mb-6">
        <label class="block text-gray-700 text-sm font-bold mb-2" for="password">Contraseña</label>
        <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="password" type="password" v-model="password" required>
      </div>

      <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded block w-full focus:outline-none focus:shadow-outline" type="submit">Iniciar Sesión</button>

      <div class="text-center mt-4">
        <a class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800" href="#">¿Olvidaste tu cuenta?</a>
        <span class="text-gray-500 mx-2">·</span>
        <a class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800" href="#">Regístrate</a>
      </div>

      <p v-if="error" class="text-red-500 font-bold text-xs italic mt-4">Error: {{ error }}</p>
    </form>
  </div>
</template>

<style scoped>

</style>