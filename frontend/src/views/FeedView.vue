<script setup>

</script>

<template>
  <div class="my-20 mx-3 flex flex-row">
    <!-- Primer elemento del feed -->
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
    <div class="basis-1/2 bg-indigo-200 m-1 ml-10 mr-10 p-2 rounded-lg shadow">
      B
    </div>

    <!-- Tercer elemento del feed -->
    <div class="basis-1/4 bg-indigo-300 m-1 rounded-lg shadow">C</div>
  </div>


</template>

<style scoped>

</style>