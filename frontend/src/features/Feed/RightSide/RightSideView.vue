<script setup>
import FriendsRecommendationsLiteView from "@/features/Feed/RightSide/FriendsRecommendationsLiteView.vue";
import FollowsRecommendationsLiteView from "@/features/Feed/RightSide/FollowsRecommendationsLiteView.vue";
</script>

<template>
  <div class="basis-1/4 bg-gray-200">

    <!--        Sugerencias de Amistad-->
    <FriendsRecommendationsLiteView />

    <!--        Sugerencias de Usuarios a Seguir  -->
    <FollowsRecommendationsLiteView />

    <!--        Sugerencias de Páginas a Seguir  -->
    <div class="invisible">
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
    <div class="invisible">
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

</template>

<style scoped>

</style>