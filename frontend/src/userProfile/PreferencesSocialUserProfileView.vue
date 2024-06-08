<script setup>
import {inject, onMounted, ref} from "vue";
import axios from "axios";
import PreferencesSocialUserProfileHashTagCard
  from "@/userProfile/PreferencesSocialUserProfileHashTagCard.vue";


const props = defineProps({
  user: String,
})

const interest = ref({});
const apiUrl = inject('apiUrl')

async function userDataLoad() {
  try {
    const response = await axios.get(
        `${apiUrl}interestsInHashTagBySocialUser/getInterestBySocialUserId/${props.user}`
    );

    interest.value = response.data.response;
    console.log("Intereses: ",interest.value)
  } catch (error) {
    console.error(error);
  }
}


onMounted(() => {
    userDataLoad(props.user);
});
</script>

<template>
  <!--      Preferencias-->
  <div class="rounded-lg shadow p-3 mt-4 bg-gray-50">
    <div class="grid grid-cols-1 divide-y">
      <div class="text-center font-bold">Preferencias</div>
        <PreferencesSocialUserProfileHashTagCard
            v-for="hashtag in interest"
            :hashtag="hashtag"
            :key="hashtag.id"
        />
    </div>
  </div>
</template>

<style scoped>

</style>