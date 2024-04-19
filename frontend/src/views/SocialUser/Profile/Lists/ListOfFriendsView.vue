<script setup>
import axios from "axios";
import {inject, onMounted, ref} from "vue";
import SocialUserToFriendCardVertical from "@/views/SocialUser/Card/SocialUserToFriendCardVertical.vue";

const props = defineProps({
  userId: String,
})

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const apiUrl = inject('apiUrl')
const socialUsers = ref([]);
const loading = ref(false);

const PAGE_SIZE = 9;
let currentPage = 0;

async function cargarDatos() {
  try {
    loading.value = true;
    const response = await axios.get(
        `${apiUrl}friend/friendsOf/${props.userId}`,
        {
          headers: {
            userId: currentUser.value.id,
          },
          params: {
            page: currentPage,
            size: PAGE_SIZE,
          },
        }
    );

    // socialUsers.value = response.data.response.content;
    // Agregar las nuevas tarjetas a la lista existente
    socialUsers.value = [...socialUsers.value, ...response.data.response.content];

    console.log('Amigos', response.data);
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  cargarDatos();
});

async function cargarMasSugerencias() {
  currentPage++; // Incrementa el número de página para cargar más tarjetas
  await cargarDatos();
}

</script>

<template>
  <div class="flex flex-wrap justify-around space-x4">
    <SocialUserToFriendCardVertical
        v-for="user in socialUsers"
        :user="user"
        :request=true
        :key="user.id"
        class=""
    />
  </div>
</template>

<style scoped>

</style>