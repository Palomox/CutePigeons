<template>
  <div class="app">
  <div class="flex flex-col">
  <mode-toggle :toggled="toggleStatus" @toggle="toggle" class="self-end"/>
  </div>
  <h1 class="text-center text-blue-500 text-3xl font-extrabold font-sans">Cute Pigeons</h1>
  <router-view/>
  <div class="flex flex-col">
    <router-link class="text-s self-end justify-self-end" to="/admin">Administrate</router-link>
  </div>
  </div>
</template>
<script lang="ts">
import {Options, Vue} from "vue-class-component";
import ModeToggle from '@/components/ModeToggle.vue'

@Options({
  components: {
    ModeToggle
  }
})
export default class App extends Vue{
  created(){
    document.documentElement.classList.add('dark')
    document.documentElement.classList.add('bg-gray-800')
  }
  toggleStatus = true
  toggle(){
    this.toggleStatus = !this.toggleStatus
    if(this.toggleStatus){
      document.documentElement.classList.add('dark')
      document.documentElement.classList.add('bg-gray-800')
    }
    if(!this.toggleStatus){
      document.documentElement.classList.remove('dark')
      document.documentElement.classList.remove('bg-gray-800')
    }
  }
}
</script>
<style>
@tailwind base;
@tailwind components;
@tailwind utilities;

.app{
  @apply text-black bg-white font-sans dark:text-white dark:bg-gray-800;
}
</style>
