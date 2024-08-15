<script setup lang="ts">
import {inject, onMounted, ref, watch} from 'vue';
import { useRouter } from 'vue-router';
import axios from "axios";
import LeftSideSocialUserProfile from "@/features/UserProfile/components/LeftSideSocialUserProfile.vue";
import RightSideView from "@/features/Feed/RightSide/RightSideView.vue";

const router = useRouter()
const socialUser = ref(null)
const props = defineProps({
    socialUserId: { require: true, type: String}
})

const currentUser = ref({});
const apiUrl = inject('apiUrl')
// url to load images
const imgUrl = inject('imgUrl');

const activeTab = ref('');

async function userDataLoad() {
  try {
    const response = await axios.get(
        `${apiUrl}socialUser/getSocialUserById/${props.socialUserId}`
    );

    socialUser.value = response.data.response;
  } catch (error) {
    console.error(error);
  }
}

onMounted(() => {
    userDataLoad()
});


watch(() => activeTab.value, (newTab) => {
  switch (newTab) {
    case "details":
      router.push({ name: 'SocialUserDetails' });
      break;
    case "friends":
      router.push({ name: 'SocialUserFriends' });
      break;
    case "follows":
      router.push({ name: 'SocialUserFollows' });
      break;
    case "followers":
      router.push({ name: 'SocialUserFollowers' });
      break;
  }
});

</script>


<template>
  <div class="my-20 mx-3 flex flex-row">
    <!-- Latelar Izquierdo -->
    <LeftSideSocialUserProfile :userId="socialUserId" />

    <!-- Zona Central -->
    <div class="basis-1/2 bg-gray-200">

      <div class="basis-1/2 bg-white mb-2 p-3 ml-10 mr-10 flex justify-around items-center">
        <!-- Botón de Publicaciones -->
        <div :class="{ 'active-tab': activeTab === 'details' }" class="text-center relative">
          <p @click="activeTab = 'details'"
             class="hover:cursor-pointer font-bold text-gray-700">Detalles</p>
          <div v-if="activeTab === 'details'" class="border-b-2 border-blue-500 absolute bottom-0 left-0 right-0"></div>
        </div>

        <!-- Botón de Amigos -->
        <div :class="{ 'active-tab': activeTab === 'friends' }" class="text-center relative">
          <p @click="activeTab = 'friends'"
             class="hover:cursor-pointer font-bold text-gray-700">Amigos</p>
          <div v-if="activeTab === 'friends'" class="border-b-2 border-blue-500 absolute bottom-0 left-0 right-0"></div>
        </div>

        <!-- Botón de Seguidos -->
        <div :class="{ 'active-tab': activeTab === 'follows' }" class="text-center relative">
          <p @click="activeTab = 'follows'"
             class="hover:cursor-pointer font-bold text-gray-700">Seguidos</p>
          <div v-if="activeTab === 'follows'" class="border-b-2 border-blue-500 absolute bottom-0 left-0 right-0"></div>
        </div>

        <!-- Botón de Seguidores -->
        <div
            :class="{ 'active-tab': activeTab === 'followers' }"
            class="text-center relative">
          <p @click="activeTab = 'followers'"
             class="hover:cursor-pointer font-bold text-gray-700">Seguidores</p>
          <div v-if="activeTab === 'followers'" class="border-b-2 border-blue-500 absolute bottom-0 left-0 right-0"></div>
        </div>
      </div>

      <div class="profile-content">
        <!-- Aquí es donde se mostraran nuestros eventos -->
        <router-view :socialUser="socialUser" />
      </div>
    </div>

    <!-- Lateral Derecho -->
<!--    <RightSideView/>-->
  </div>
</template>


<style scoped>
.active-tab {
  color: #4CAF50; /* Color activo */
}

.active-tab:hover {
  cursor: pointer;
  opacity: 0.8; /* Reducir opacidad al pasar el mouse */
}

/* Estilo para el borde inferior */
.border-b-2 {
  border-bottom-width: 2px;
}
</style>