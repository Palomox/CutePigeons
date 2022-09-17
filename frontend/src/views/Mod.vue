<template>
  <nav-bar/>
  <div class="flex flex-row m-8">
    <pigeon-shower v-for="post in posts" type="mod" :post="post"/>
  </div>
  <div class="bottom-2 left-2 absolute bg-blue-900 w-16 h-16 shadow shadow-md rounded-full"
       @click="$router.push('/')">
    <font-awesome-icon class="text-white text-4xl relative transform w-full translate-y-[40%]" icon="fa-solid fa-house"/>
  </div>
</template>

<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
import PigeonShower from "@/components/PigeonShower.vue";
import {Post} from "@/Types";
import {onMounted, ref} from "vue";
import {modApi} from "@/store/apis";

onMounted(() => {
  modApi.getModQueue().then(({data}) => {
    if(!data.posts){
      return;
    }
    posts.value = data.posts?.map((post) => {
      let id = post.id || 0;
      let title = post.title || "";
      let author = post.author || "";
      let url = post.url || "";
      let likes = post.likes || 0;
      let liked = post.liked || false;

      return new Post(id, title, author, url, likes, liked);
    });
  })
});

let posts = ref<Post[]>([
  {
    title: "Smart pigeon",
    author: "Palomox",
    url: "https://cdn.download.ams.birds.cornell.edu/api/v1/asset/159976171/1800",
    id: 4,
    likes: 5,
    liked: false
  },
])

</script>

<style scoped>

</style>
