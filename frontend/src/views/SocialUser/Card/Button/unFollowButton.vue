<script setup>
import {ref} from "vue";
import BaseTooltip from "@/views/utils/BaseTooltip.vue";
import FollowService from "@/services/follow-service.js";

// Props
const props = defineProps({
  userIdTarget: String
})

// Hover state
const hover = ref({follow:false});

// Emits
const emit = defineEmits(['updateStatusFollow'])

// Llamada para dejar de seguir a un usuario
async function unFollowUser() {
  FollowService.unfollowUser(props.userIdTarget)
      .then(data => {
        console.log('Usuario dejado de seguir con éxito:', data);
        emit('updateStatusFollow', false);
      })
      .catch(error => {
        console.error('Error al dejar de seguir al usuario:', error);
      });
}

</script>

<template>
  <!--  unFollow Button-->
    <BaseTooltip
        :content="$t('unfollowMessage')"
        placement="bottom">
          <button
              @click="unFollowUser()"
              @mouseover="hover.follow = true"
              @mouseout="hover.follow = false"
              class="button-vertical bg-white text-black
                hover:bg-red-100 hover:text-red-600 hover:border-red-400
                focus:outline-none focus:shadow-outline">
            <span v-if="!hover.follow">
              {{ $t('following') }}
            </span>
            <span v-else>
              <fa icon="fa-solid fa-ban" class="text-red-600 mr-1"/>
              {{ $t('follow') }}
            </span>
          </button>
    </BaseTooltip>

</template>

<style scoped>

</style>