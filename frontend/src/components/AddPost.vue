<template>
  <div v-if="props.show" class="fixed w-full h-full bg-gray-900 bg-opacity-50 flex flex-col items-center justify-center z-50" >
    <div id="wrapper" class="bg-gray-700 grid rounded-md p-4">
      <div class="col-1 row-1 text-blue-700 text-3xl font-medium">
        New post
      </div>
      <div class="col-2 row-1 flex flex-row-reverse">
        <font-awesome-icon @click.prevent="$emit('menuClosed')" icon="fa-solid fa-xmark" class="text-2xl text-gray-400"/>
      </div>
      <div class="col-1 row-2 flex flex-col">
        <label class="text-purple-500 text-2xl">Title</label>
        <input v-model="adding.title" class="text-black rounded-md p-0.5" type="text"/>
      </div>
      <div class="col-start-1 row-3 flex flex-col">
        <label class="text-2xl text-purple-500">Image</label>
        <div @click="selectImage" :class="getDropboxClasses" @dragenter.prevent.stop="choosing = true" @dragover.prevent.stop @dragleave.prevent.stop="choosing = false" @drop.prevent.stop="imageDropped">
          <span class="text-gray-500">Drop your image here</span>
          <span class="text-gray-500">or</span>
          <input ref="fileselector" type="file" accept="image/*" @change="handleFile($event.target.files[0])" class="hidden">
          <button class="bg-blue-700 rounded-md text-3xl p-2 pl-10 pr-10" >Select image</button>
        </div>
      </div>
      <div class="col-start-1 col-end-3 row-4 items-center flex flex-row">
        <button @click.prevent="uploadPigeon" class="bg-blue-700 text-white flex-auto p-2 rounded-md max-h-14 text-3xl">
          Upload Pigeon
        </button>
      </div>
      <div class="col-start-2 col-end-2 row-start-2 row-end-5">
        <img v-if="adding.image" :src="adding.image" class="max-h-[90%] max-w-[80%] h-auto ml-6 rounded-md border border-white"/>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {V0alpha2Api} from "@ory/client";
const props = defineProps({
  show: {type: Boolean, required: true}
});

const emit = defineEmits<{
  (e: "menuClosed") : void
  (e: "postAdded", title: string, image: Blob) : void
}>();

let choosing = ref(false);



let adding = ref({
  title: "",
  image: ""
});

let image = ref<Blob | null>(null)

function imageDropped(event: DragEvent) {
  if (event.dataTransfer) {
    handleFile(event.dataTransfer.files[0]);
  }
  choosing.value = false;
}

function handleFile(file: File) {
  if(file.type.startsWith("image/")){
    image.value = file;
    adding.value.image = URL.createObjectURL(file);
  }
}

const getDropboxClasses = computed(() => {
  return choosing.value ? "bg-blue-200 ring rounded-md flex flex-col justify-end items-center flex-auto mu-2 pb-3" : "bg-gray-200 rounded-md flex flex-col justify-end items-center flex-auto mu-2 pb-3";
});

function uploadPigeon(){
  emit("postAdded", adding.value.title, image.value as Blob);
  emit("menuClosed");

  URL.revokeObjectURL(adding.value.image);

  adding.value = {
    title: "",
    image: ""
  }
}

let fileselector = ref<HTMLInputElement | null>(null);

const selectImage = () => {
  fileselector.value?.click();
}

</script>

<style scoped>
  #wrapper{
    grid-template-columns: 25rem 30rem;
    grid-template-rows: 3rem 4rem 15rem 4rem;
  }
</style>
