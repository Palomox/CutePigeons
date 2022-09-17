<template>
  <add-post @post-added="(title, file) => createPost(title, file)" :show="adding" @menu-closed="adding=false"/>
  <nav-bar/>
  <div class="m-8 home flex flex-row gap-5 items-start">
    <pigeon-shower type="user" :editable="editablePosts.includes(post.id)" @do-like="(id) => doLike(id)"
                   @delete="(id) => deletePost(id)" v-for="post in posts" :post="post"/>
  </div>
  <div v-if="logged"
      class="bottom-2 right-2 absolute bg-blue-900 w-16 h-16 shadow shadow-md rounded-full"
      @click="adding = true">
    <font-awesome-icon class="text-white text-4xl relative w-full transform translate-y-[40%]"  icon="fa-solid fa-plus"/>
  </div>
  <div v-if="isMod"
      class="bottom-2 left-2 absolute bg-blue-900 w-16 h-16 shadow shadow-md rounded-full"
      @click="$router.push('/mod')">
    <font-awesome-icon class="text-white text-4xl relative transform w-full translate-y-[40%]" icon="fa-solid fa-binoculars"/>
  </div>
</template>
<script setup lang="ts">
import PigeonShower from '@/components/PigeonShower.vue'
import {Post} from "@/Types";
import {computed, onMounted, ref} from "vue";
import NavBar from "@/components/NavBar.vue";
import { Session } from "@ory/client";
import AddPost from "@/components/AddPost.vue";
import {useToast} from "vue-toastification";
import {ory as ory, postsApi, userApi} from "@/store/apis";


let session: Session | null = null;
let logoutUrl: String = "";


onMounted(() => {
  ory.toSession().then(({data}) => {
    session = data;
    logged.value = true;

    loadPosts()

    ory.createSelfServiceLogoutFlowUrlForBrowsers().then(({data}) => {
      logoutUrl = data.logout_url;
    })
  })
})

const posts = ref<Post[]>([
  {
    title: "Smart pigeon",
    author: "Palomox",
    url: "https://cdn.download.ams.birds.cornell.edu/api/v1/asset/159976171/1800",
    id: 4,
    likes: 5,
    liked: false
  },
  {
    title: "Cute couple",
    author: "Palomox",
    url: "https://cdn.discordapp.com/attachments/569629084976414724/1017558572319645796/404F7682-14B6-49B2-80F1-598DF11FEE77.webp",
    id: 5,
    likes: 7,
    liked: true
  },
  {
    title: "Wood pigeon",
    author: "Palomox",
    id: 6,
    url: "https://cdn.download.ams.birds.cornell.edu/api/v1/asset/242173971/1200",
    likes: 18,
    liked: false
  }
]);


const adding = ref(false);

const toast = useToast();

let editablePosts: number[] = [];

let logged = ref(false);

function deletePost(id: number) {
  postsApi.deletePost(id).then(() => {
    posts.value = posts.value.filter((post) => post.id !== id);
  });
}

function loadPosts() {
  postsApi.getPosts().then(({data}) => {
    if (data.posts) {
      data.posts.forEach((post) => {
        let id = post.id || 0;
        let title = post.title || "";
        let url = post.url || "";
        let author = post.author || "";
        let likes = post.likes || 0;
        let liked = post.liked || false;

        posts.value.push(new Post(id, title, url, author, likes, liked));
      })
    }
  })

  postsApi.getPosts(true).then(({data}) => {
    if (data.posts) {
      data.posts.forEach((post) => {
        let id = post.id || 0;
        editablePosts.push(id);
      })
    }
  })

}

function createPost(title: string, file: File) {
  postsApi.addPostForm(title, file).then((data) => {
    if (data.status == 200) {
      toast.success("Post added successfully");
    }
  })
}

function doLike(id: number) {
  if(session == null || session.active == false) {
    toast.error("You need to be logged in to like a post");
    return;
  }

  postsApi.likePost(id).then((data) => {
        if (data.status == 200) {
          let post = posts.value.find((post) => post.id == id);
          if (post) {
            let likes = data.data.likes || post.likes;
            let liked = data.data.status?.includes("Added") ? true : false;
            post.likes = likes;
            post.liked = liked;
          }
        }
      }
  )
}

const isMod = computed(() => {
  if (session == null) {
    return false;
  }

  return userApi.getUserDetails().then(({data}) => {
    return data.group == "admin" || "moderator";
  })
})
</script>
<style>
@tailwind base;
@tailwind components;
@tailwind utilities;
html {
  @apply bg-gray-800 text-white font-sans;
}
</style>
