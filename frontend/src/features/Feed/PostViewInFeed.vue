<script setup>
import { onMounted, ref, inject } from "vue";
import PostView from "@/features/Posts/PostViewInFeed/PostView.vue";

const posts = ref([]);
const currentPage = ref(0);
const totalPages = ref(0);

const apiUrl = inject('apiUrl')

async function cargarDatos() {
  try {
    const response = await fetch(apiUrl + 'post/getAllPosts');
    const data = await response.json();
    posts.value = data.response.content;
    currentPage.value = data.response.pageable.pageNumber;
    totalPages.value = data.response.pageable.pageSize;

  } catch (e) {
    console.error(e);
  }
}

onMounted(() => {
  cargarDatos();
});

</script>

<template>
  <div class="flex flex-col">
    <PostView
        v-for="post in posts"
        :post="post"
        :key="post.id"
    />
  </div>
</template>

<style scoped>

</style>