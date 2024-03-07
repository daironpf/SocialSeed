<script setup>

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


    </div>

    <!-- Lateral Derecho -->
    <div class="basis-1/4 bg-gray-200">

      <div class="flex flex-row justify-between">
<!--        Sugerencias de Amistad-->
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

  </div>


</template>

<style scoped>

</style>