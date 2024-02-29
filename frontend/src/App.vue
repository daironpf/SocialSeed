<script setup>
import {RouterView, RouterLink} from "vue-router";

</script>

<template lang="html">
  <main>
    <div class="links">
      <RouterLink active-class="active" to="/">Home</RouterLink>
      <RouterLink active-class="active" to="/about">About</RouterLink>
    </div>
    <RouterView />
  </main>
</template>



<style scoped>
.links {
  padding: 20px
}

.links a {
  margin: 0 5px
}

.active{
  font-weight: bold;
  background-color: burlywood;
}
</style>