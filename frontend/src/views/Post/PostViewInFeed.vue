<script setup>
import { onMounted, ref, inject } from "vue";
import PostView from "@/views/Post/PostViewInFeed/PostView.vue";

const posts = ref([]);
const currentPage = ref(0);
const totalPages = ref(0);

const apiUrl = inject('apiUrl')

async function cargarDatos() {
  try {
    const response = await fetch(apiUrl + 'post/getAllPosts');
    const data = await response.json();

    console.log(data)
    console.log('Status: '+data.status)
    console.log('Content: '+data.message)
    console.log('Version: '+data.version)
    posts.value = data.response.content;
    console.log('Posts: ',posts.value)

    console.log('isEmpty: ',data.response.empty)
    console.log('isFirst: ',data.response.first)
    console.log('isLast: ',data.response.last)
    console.log('size: ',data.response.size)
    console.log('total of Elements: ',data.response.totalElements)
    console.log('total of Pages: ',data.response.totalPages)

    console.log('number of page: ',data.response.totalPages)
    console.log('size of page: ',data.response.totalPages)

    currentPage.value = data.response.pageable.pageNumber;
    totalPages.value = data.response.pageable.pageSize;

  } catch (e) {
    console.error(e);
  }
}

onMounted(() => {
  cargarDatos();
});

</script>

<template>
  <div class="flex flex-col">
    <PostView
        v-for="post in posts"
        :post="post"
        :key="post.id"
    />


    <!--        publicacion N-->
    <div class="bg-gray-50 rounded-lg shadow-lg m-1 ml-10 mr-10 p-4">
      <!-- Zona del usuario autor del post -->
      <div class="flex items-center mb-4">
        <img src="/img/tesla.png" alt="Foto de perfil de Nikola Tesla" class="w-12 h-12 rounded-full mr-4">
        <div>
          <p class="font-bold">Nikola Tesla</p>
          <p class="text-gray-700">Innovador incansable con una mente brillante y una visión revolucionaria para el futuro.</p>
        </div>
      </div>
      <div>
        <p class="text-lg text-gray-800 text-lg">
          La corriente alterna es el faro del mañana, una innovación que iluminará el mundo. Su eficiencia y potencia desafiarán el antiguo paradigma eléctrico, llevando a la humanidad hacia un futuro brillante y sostenible.
        </p>
      </div>
      <!-- Zona de la botonera del post -->
      <!--      Botones para personalizar el Post-->
      <div class="max-w-lg mx-auto mt-3">
        <div class="flex flex-row">
          <div class="basis-1/12"></div>
          <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
            <fa icon="fa-solid fa-thumbs-up" class="text-gray-400"/>
            <p class="ml-2">Me Gusta</p>
          </div>
          <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
            <fa icon="fa-solid fa-comment" class="text-gray-400"/>
            <p class="ml-2">Comentar</p>
          </div>
          <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
            <fa icon="fa-solid fa-comment" class="text-gray-400"/>
            <p class="ml-2">Compartir</p>
          </div>
          <div class="basis-1/12"></div>
        </div>
      </div>
    </div>

    <!--        publicacion N-1 -->
    <div class="bg-gray-50 rounded-lg shadow-lg m-1 ml-10 mr-10 p-4">
      <!-- Zona del usuario autor del post -->
      <div class="flex items-center mb-4">
        <img src="/img/alberteinstein.png" alt="Foto de perfil de Nikola Tesla" class="w-12 h-12 rounded-full mr-4">
        <div>
          <p class="font-bold">Albert Einstein</p>
          <p class="text-gray-700">Soy una mente inquisitiva y curiosa, siempre en búsqueda de la verdad y desafiando las convenciones establecidas</p>
        </div>
      </div>
      <div>
        <p class="text-lg text-gray-800 text-lg">
          La teoría de la relatividad es como un viaje fascinante a través del tiempo y el espacio, donde las reglas familiares de la física se desdibujan, revelando un universo de sorpresas y maravillas. En este intrincado tejido cósmico, la gravedad y la luz danzan en un ballet de interacciones que desafían nuestra percepción del mundo. En mi humilde opinión, la relatividad no solo es una teoría científica, sino una ventana hacia la belleza y la complejidad del universo mismo.
        </p>
      </div>
      <!-- Zona de la botonera del post -->
      <!--      Botones para personalizar el Post-->
      <div class="max-w-lg mx-auto mt-3">
        <div class="flex flex-row">
          <div class="basis-1/12"></div>
          <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
            <fa icon="fa-solid fa-thumbs-up" class="text-gray-400"/>
            <p class="ml-2">Me Gusta</p>
          </div>
          <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
            <fa icon="fa-solid fa-comment" class="text-gray-400"/>
            <p class="ml-2">Comentar</p>
          </div>
          <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
            <fa icon="fa-solid fa-comment" class="text-gray-400"/>
            <p class="ml-2">Compartir</p>
          </div>
          <div class="basis-1/12"></div>
        </div>
      </div>
    </div>

    <!--        publicacion N-2 -->
    <div class="bg-gray-50 rounded-lg shadow-lg m-1 ml-10 mr-10 p-4">
      <!-- Zona del usuario autor del post -->
      <div class="flex items-center mb-4">
        <img src="/img/tesla.png" alt="Foto de perfil de Nikola Tesla" class="w-12 h-12 rounded-full mr-4">
        <div>
          <p class="font-bold">Nikola Tesla</p>
          <p class="text-gray-700">Innovador incansable con una mente brillante y una visión revolucionaria para el futuro.</p>
        </div>
      </div>
      <div>
        <p class="text-lg text-gray-800 text-lg">
          La corriente alterna es el faro del mañana, una innovación que iluminará el mundo. Su eficiencia y potencia desafiarán el antiguo paradigma eléctrico, llevando a la humanidad hacia un futuro brillante y sostenible.
        </p>
      </div>
      <!-- Zona de la botonera del post -->
      <!--      Botones para personalizar el Post-->
      <div class="max-w-lg mx-auto mt-3">
        <div class="flex flex-row">
          <div class="basis-1/12"></div>
          <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
            <fa icon="fa-solid fa-thumbs-up" class="text-gray-400"/>
            <p class="ml-2">Me Gusta</p>
          </div>
          <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
            <fa icon="fa-solid fa-comment" class="text-gray-400"/>
            <p class="ml-2">Comentar</p>
          </div>
          <div class="basis-3/12 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
            <fa icon="fa-solid fa-comment" class="text-gray-400"/>
            <p class="ml-2">Compartir</p>
          </div>
          <div class="basis-1/12"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>