<script setup>
import ProfileView from "@/views/Feed/LeftSide/ProfileView.vue";
import PreferencesSocialUserProfileView from "@/views/SocialUser/Profile/PreferencesSocialUserProfileView.vue";

const props = defineProps({
  userId: String,
})

</script>

<template>
  <div class="basis-1/4  m-1 bg-gray-200">
    <ProfileView :user="props.userId" />

    <PreferencesSocialUserProfileView :user="props.userId" />
  </div>
</template>

<style scoped>

</style>