<template>
  <div class="flex flex-col cursor-pointer" @click="doLike">
    <span class="text-pink-200 self-center text-xl">{{ props.likes }}</span>
      <font-awesome-icon class="text-pink-200 text-2xl" :icon="getIcon"/>
  </div>
</template>

<script setup lang="ts">

import {computed, ref} from "vue";

const props = defineProps({
  likes: {type: Number, required: true},
  liked: {type: Boolean, required: true}
});

const emit = defineEmits<{
  (e: "doLike") : void
}>();


const getIcon = computed(() => {
  if (props.liked) {
    return "fa-solid fa-heart";
  } else {
    return "fa-regular fa-heart";
  }
});

function doLike(){
  emit("doLike");
}
</script>

<style scoped>

</style>
