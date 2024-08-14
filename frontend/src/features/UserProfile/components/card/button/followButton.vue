<script setup>
import BaseTooltip from "@/components/BaseTooltip.vue";
import FollowService from "@/core/a mover/follow-service.ts";

// Props
const props = defineProps({
  userIdTarget: String
})

// Emits
const emit = defineEmits(['updateStatusFollow'])

// Function to follow a user
async function followUser() {
  FollowService.followUser(props.userIdTarget)
      .then(data => {
        console.log('Following user successful:', data);
        emit('updateStatusFollow', true);
      })
      .catch(error => {
        console.error('Error in following user:', error);
      });
}
</script>

<template>
  <!--  Follow button-->
  <BaseTooltip
      :content="$t('followMessage')"
      placement="bottom">
        <button
            @click="followUser()"
            class="button-vertical bg-blue-300 text-white
                  hover:bg-blue-500
                  focus:outline-none focus:shadow-outline
                  ">
          <fa icon="fa-solid fa-person-circle-plus" class="text-white-600"/>
          {{ $t('follow') }}
        </button>
  </BaseTooltip>
</template>

<style scoped>

</style>