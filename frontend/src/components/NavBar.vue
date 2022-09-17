<template>
  <div class="bg-gray-700 max-h-12 text-blue-600 shadow shadow-md font-bold text-3xl pl-2 pt-1 pb-1 flex flex-row">
    <router-link to="/">CutePigeons</router-link>
    <session-manager class="ml-auto mr-2"/>
  </div>
</template>

<script setup lang="ts">
import SessionManager from "@/components/SessionManager.vue";
</script>

<style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;
</style>
