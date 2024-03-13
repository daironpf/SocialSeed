<script setup>
import { onMounted, ref, inject } from "vue";
import PostView from "@/views/Post/PostViewInFeed/PostView.vue";

const posts = ref([]);
const currentPage = ref(0);
const totalPages = ref(0);

const apiUrl = inject('apiUrl')

async function cargarDatos() {
  try {
    const response = await fetch(apiUrl + 'post/getAllPosts');
    const data = await response.json();

    console.log(data)
    console.log('Status: '+data.status)
    console.log('Content: '+data.message)
    console.log('Version: '+data.version)
    posts.value = data.response.content;
    console.log('Posts: ',posts.value)

    console.log('isEmpty: ',data.response.empty)
    console.log('isFirst: ',data.response.first)
    console.log('isLast: ',data.response.last)
    console.log('size: ',data.response.size)
    console.log('total of Elements: ',data.response.totalElements)
    console.log('total of Pages: ',data.response.totalPages)

    console.log('number of page: ',data.response.totalPages)
    console.log('size of page: ',data.response.totalPages)

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