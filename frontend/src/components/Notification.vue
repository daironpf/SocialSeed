<script setup>
import { ref, onMounted, onUserEvent } from 'vue';

const props = defineProps({
  message: String
});

const notificationVisible = ref(false);

onUserEvent('userFollowed', (message) => {
  showNotification(message);
});

onMounted(() => {
  // Ocultar la notificación después de 5 segundos
  setTimeout(() => {
    notificationVisible.value = false;
  }, 5000);
});

function showNotification(message) {
  props.message = message;
  notificationVisible.value = true;
}
</script>

<template>
  <!-- Ventana emergente de notificación -->
  <div v-if="notificationVisible" class="fixed inset-x-0 bottom-0 z-5 flex justify-center mb-4">
    <div class="bg-green-400 text-white p-4 rounded-md shadow-md">
      {{ notificationMessage }}
    </div>
  </div>
</template>

<style scoped>

</style>