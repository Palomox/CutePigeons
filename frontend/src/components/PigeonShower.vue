<template>
  <div class="flex flex-col bg-gray-700 max-w-[33%] p-3 rounded-md shadow-md">
    <div class="flex flex-row">
      <div>
        <h2 @contextmenu.prevent.stop @mousedown.prevent="handleContextMenu"  class="text-3xl text-purple-500 font-semibold cursor-default">{{ props.post.title }}</h2>
        <context-menu v-if="editable" :type="getContextMenu" :open="showDelete" @mod-action="(allow) => modAction(allow)" @delete="deletePost" />
        <h3 class="text-pink-200 mb-1">{{ getAuthor }}</h3>
      </div>
      <div v-if="type == 'user'" class="ml-auto">
        <likes :likes="props.post.likes" :liked="props.post.liked" @do-like="doLike"/>
      </div>
    </div>
    <img class="self-center h-auto rounded-md" :src="props.post.url">
  </div>
</template>

<script setup lang="ts">
import {Post} from "../Types";
import {computed, ref} from "vue";
import Likes from "@/components/Likes.vue";
import ContextMenu from "@/components/ContextMenu.vue";
import {modApi} from "@/store/apis";
import {useToast} from "vue-toastification";

const props = defineProps({
  post: {type: Post, required: true},
  editable: {type: Boolean, default: false},
  type: {type: String, required: true}
});

const toast = useToast();

const emit = defineEmits<{
  (e: "delete", id: number) : void,
  (e: "doLike", id: number) : void
}>();

const getAuthor = computed(() => {
  return "uploaded by " + props.post.author;
});

let showDelete = ref(false);

function deletePost(){
  showDelete.value = false;
  emit("delete", props.post.id);
}

function doLike(){
  emit("doLike", props.post.id);
}

function handleContextMenu(event: MouseEvent) {
  event.button === 2 ? showDelete.value = !showDelete.value : showDelete.value = false;
}

function modAction(allowed : boolean){
  showDelete.value = false;
  modApi.doModAction({allow: allowed}, props.post.id).then((response) => {
    switch(response.data.allowed){
      case true:
        toast.success(response.data.message);
        break;
      case false:
        toast.error(response.data.message);
        break;
    }
  });
}

const getContextMenu = computed(() => {
  return props.type.includes("mod") ? "modactions" : "delete";
});
</script>

<style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;

</style>
