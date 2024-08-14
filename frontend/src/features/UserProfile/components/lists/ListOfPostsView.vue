<script setup>
import axios from "axios";
import {inject, onMounted, ref} from "vue";
import PostView from "@/posts/PostViewInFeed/PostView.vue";

const props = defineProps({
  userId: String,
})

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')));
const apiUrl = inject('apiUrl')
const posts = ref([]);
const loading = ref(false);

const PAGE_SIZE = 9;
let currentPage = 0;

async function cargarDatos() {
  try {
    loading.value = true;
    const response = await axios.get(
        `${apiUrl}post/getAllPostsByUserId/${props.userId}`,
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
    posts.value = [...posts.value, ...response.data.response.content];

    console.log('Publicaciones', response.data);
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
    <PostView
        v-for="post in posts"
        :post="post"
        :key="post.id"
    />
  </div>
</template>

<style scoped>

</style>