<template>
  <div class="app">
    <router-view/>
  </div>
</template>
<script setup lang="ts">
</script>
<style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;

.app {
  @apply font-sans text-white bg-gray-800;
}
</style>
