<script setup>

import PostViewInFeed from "@/views/Post/PostViewInFeed.vue";
</script>

<template>
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <div class="basis-1/4  m-1 bg-gray-200">
      <div class="rounded-lg shadow w-30 bg-gray-50">
<!--        Perfil-->
          <div class="bg-cover  h-20 rounded-t-lg" style="background-image: url('/img/background-user-test.jpg')"></div>
          <div class="p-4">
            <img src="/img/user-perfil.jpg" class="rounded-full w-16 h-16 border border-white border-2 -mt-11 mx-auto" alt="">
            <h1 class="text-xl font-bold text-color-black text-center mt-2">Dairon Pérez Frías</h1>
            <p class="text-gray-700 text-center">Me encantan las mascotas, especialmente los perritos.</p>
          </div>
      </div>
<!--      Preferencias-->
      <div class="rounded-lg shadow p-3 mt-4 bg-gray-50">
        <div class="grid grid-cols-1 divide-y">
          <div class="text-center font-bold">Preferencias</div>
          <div class="flex justify-between items-center">
            <p class="font-bold"># Futbol </p><span class="bg-blue-200 rounded-full px-2 text-white self-end">30</span>
          </div>
          <div class="flex justify-between items-center">
            <p class="font-bold"># ChatGPT </p><span class="bg-blue-200 rounded-full px-2 text-white self-end">10</span>
          </div>
          <div class="flex justify-between items-center">
            <p class="font-bold"># One Piece </p><span class="bg-blue-200 rounded-full px-2 text-white self-end">134</span>
          </div>
          <div class="flex justify-between items-center">
            <p class="font-bold"># Memes </p><span class="bg-blue-200 rounded-full px-2 text-white self-end">534</span>
          </div>
        </div>
      </div>

      <!-- Publicaciones -->
      <div class="rounded-lg shadow p-3 mt-4 bg-gray-50">
        <div class="grid grid-cols-1 divide-y">
          <div class="text-center font-bold">Publicaciones</div>
          <div class="flex justify-between items-center">
            <p class="font-bold">Amigos </p><span class="bg-blue-200 rounded-full px-2 text-white">30</span>
          </div>
          <div class="flex justify-between items-center">
            <p class="font-bold">Seguidos </p><span class="bg-blue-200 rounded-full px-2 text-white">10</span>
          </div>
          <div class="flex justify-between items-center">
            <p class="font-bold">Grupos </p><span class="bg-blue-200 rounded-full px-2 text-white">134</span>
          </div>
          <div class="flex justify-between items-center">
            <p class="font-bold">Páginas </p><span class="bg-blue-200 rounded-full px-2 text-white">534</span>
          </div>
        </div>
      </div>

    </div>

    <!-- Zona de Posts en el Feed -->
    <div class="basis-1/2 bg-gray-200">
      <div class="bg-gray-50 rounded-lg shadow m-1 ml-10 mr-10 p-2">
        <div class="max-w-lg mx-auto flex items-center border-b border-gray-300 pb-4 mb-4">
          <img src="/img/user-perfil.jpg" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
          <button class="flex-1 px-4 py-2 bg-gray-50 text-gray-500 rounded-full border border-gray-400 hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
            Crear publicación
          </button>
        </div>

        <!--      Botones para personalizar el Post-->
        <div class="max-w-lg mx-auto">
          <div class="flex flex-row">
            <div class="basis-1/6"></div>
            <div class="basis-2/6 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100">
              <fa icon="fa-solid fa-image" class="text-blue-500"/>
              <p class="ml-2">Imagen</p>
            </div>
            <div class="basis-2/6 flex items-center mb-4 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 ml-4">
              <fa icon="fa-solid fa-clock" class="text-blue-500"/>
              <p class="ml-2">Programar</p>
            </div>
            <div class="basis-1/6"></div>
          </div>
        </div>
      </div>

<!--      Zona de Publicaciones del Feed-->
      <div class="flex flex-col">
        <PostViewInFeed />
      </div>


    </div>

    <!-- Lateral Derecho -->
    <div class="basis-1/4 bg-gray-200">

      <!--        Sugerencias de Amistad-->
      <div>
        <div class="flex flex-row justify-between">

          <p class="font-bold">Pedir Amistad</p>
          <p>
            <fa icon="fa-solid fa-sync" class="text-gray-500 "/>
          </p>
        </div>
  <!--      Sugerencia N -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2">
  <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
  <!--          Encabezado-->
            <img src="/img/vegapunk.jfif" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>VegaPunk</p>
                <span class="text-xs">SocialUser</span>
              </div>
              <div class="flex pt-3">
                <!-- Botonera -->
                <span class="ml-2 mr-2 bg-sky-200">Pedir Amistad</span>
                <span class="ml-2 mr-2 bg-sky-200"> C </span>
              </div>
            </div>
          </div>

        </div>

        <!--      Sugerencia N-1 -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2 mt-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/vegapunk.jfif" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>VegaPunk</p>
                <span class="text-xs">SocialUser</span>
              </div>
              <div class="flex pt-3">
                <!-- Botonera -->
                <span class="ml-2 mr-2 bg-sky-200">Pedir Amistad</span>
                <span class="ml-2 mr-2 bg-sky-200"> C </span>
              </div>
            </div>
          </div>
        </div>

        <!--      Sugerencia N-2 -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2 mt-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/vegapunk.jfif" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>VegaPunk</p>
                <span class="text-xs">SocialUser</span>
              </div>
              <div class="flex pt-3">
                <!-- Botonera -->
                <span class="ml-2 mr-2 bg-sky-200">Pedir Amistad</span>
                <span class="ml-2 mr-2 bg-sky-200"> C </span>
              </div>
            </div>
          </div>
        </div>
        <a href="">Ver todas las recomendaciones</a>
      </div>

      <!--        Sugerencias de Usuarios a Seguir  -->
      <div>
        <div class="flex flex-row justify-between mt-5 b-2">

          <p class="font-bold">Usuarios a Seguir</p>
          <p>
            <fa icon="fa-solid fa-sync" class="text-gray-500 "/>
          </p>
        </div>
        <!--      Sugerencia N -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/vegapunk.jfif" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>VegaPunk</p>
                <span class="text-xs">SocialUser</span>
              </div>
              <div class="flex justify-between pt-3">
                <!-- Botonera -->
                <div class="bg-sky-200">Seguir</div>
                <div class="ml-4 bg-gray-400">Te Sigue</div>
                <div class="ml-4 bg-sky-200"> C </div>
              </div>
            </div>
          </div>

        </div>

        <!--      Sugerencia N-1 -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2 mt-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/vegapunk.jfif" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>VegaPunk</p>
                <span class="text-xs">SocialUser</span>
              </div>
              <div class="flex justify-between pt-3">
                <!-- Botonera -->
                <div class="bg-sky-200">Seguir</div>
                <div class="ml-4 bg-sky-200"> C </div>
              </div>
            </div>
          </div>
        </div>

        <!--      Sugerencia N-2 -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2 mt-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/tesla.png" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>Nikola Tesla</p>
                <span class="text-xs">SocialUser</span>
              </div>
              <div class="flex justify-between pt-3">
                <!-- Botonera -->
                <div class="bg-sky-200">Seguir</div>
                <div class="ml-4 bg-gray-400">Te Sigue</div>
                <div class="ml-4 bg-sky-200"> C </div>
              </div>
            </div>
          </div>
        </div>
        <a href="">Ver todas las recomendaciones</a>
      </div>

      <!--        Sugerencias de Páginas a Seguir  -->
      <div>
        <div class="flex flex-row justify-between mt-5 b-2">

          <p class="font-bold">Páginas a Seguir</p>
          <p>
            <fa icon="fa-solid fa-sync" class="text-gray-500 "/>
          </p>
        </div>
        <!--      Sugerencia N -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/vegapunk.jfif" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>VegaPunk</p>
                <span class="text-xs">Página</span>
              </div>
              <div class="flex justify-between pt-3">
                <!-- Botonera -->
                <div class="bg-sky-200">Seguir</div>
                <div class="ml-4 bg-sky-200"> C </div>
              </div>
            </div>
          </div>

        </div>

        <!--      Sugerencia N-1 -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2 mt-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/vegapunk.jfif" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>VegaPunk</p>
                <span class="text-xs">Página</span>
              </div>
              <div class="flex justify-between pt-3">
                <!-- Botonera -->
                <div class="bg-sky-200">Seguir</div>
                <div class="bg-sky-200"> C </div>
              </div>
            </div>
          </div>
        </div>

        <!--      Sugerencia N-2 -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2 mt-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/tesla.png" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>Nikola Tesla</p>
                <span class="text-xs">Página</span>
              </div>
              <div class="flex justify-between pt-3">
                <!-- Botonera -->
                <div class="bg-sky-200">Seguir</div>
                <div class="bg-sky-200"> C </div>
              </div>
            </div>
          </div>
        </div>
        <a href="">Ver todas las recomendaciones</a>
      </div>

      <!--        Sugerencias de Grupos a Unirse  -->
      <div>
        <div class="flex flex-row justify-between mt-5 b-2">

          <p class="font-bold">Grupos a Unirse</p>
          <p>
            <fa icon="fa-solid fa-sync" class="text-gray-500 "/>
          </p>
        </div>
        <!--      Sugerencia N -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/vegapunk.jfif" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>Punk</p>
                <span class="text-xs">Grupo</span>
              </div>
              <div class="flex justify-between pt-3">
                <!-- Botonera -->
                <div class="bg-sky-200">+</div>
                <div class="ml-4 bg-sky-200"> C </div>
              </div>
            </div>
          </div>

        </div>

        <!--      Sugerencia N-1 -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2 mt-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/vegapunk.jfif" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>Amantes de One Piece</p>
                <span class="text-xs">Grupo</span>
              </div>
              <div class="flex pt-3">
                <!-- Botonera -->
                <div class="bg-sky-200">+</div>
                <div class="ml-3 bg-sky-200"> C </div>
              </div>
            </div>
          </div>
        </div>

        <!--      Sugerencia N-2 -->
        <div class="bg-gray-50 rounded-lg shadow ml-3 mr-3 p-2 mt-2">
          <!--        Zona de Recomendaciones para Amigos-->
          <div class="flex flex-row">
            <!--          Encabezado-->
            <img src="/img/tesla.png" alt="Foto de usuario" class="w-12 h-12 rounded-full mr-4">
            <div>
              <div>
                <p>Grandes Cientificos</p>
                <span class="text-xs">Grupo</span>
              </div>
              <div class="flex pt-3">
                <!-- Botonera -->
                <div class="bg-sky-200">+</div>
                <div class="ml-3 bg-sky-200"> C </div>
              </div>
            </div>
          </div>
        </div>
        <a href="">Ver todas las recomendaciones</a>
      </div>
    </div>

  </div>


</template>

<style scoped>

</style>