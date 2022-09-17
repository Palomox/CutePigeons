<template>
  <div class="flex flex-row items-center">
    <div v-if="session">
      <!-- Logged in-->
      <router-link to="/user">
        <label class="cursor-pointer text-2xl font-medium">{{ session?.identity.traits.username }}</label>
      </router-link>
    </div>
    <div v-else>
      <!-- Not logged in-->
      <button class="bg-blue-700 text-white rounded-md text-2xl p-1" @click="login">Log in</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {Session} from "@ory/client";
import {ory, basePath, getLoginLink} from "@/store/apis";
import {computed, onMounted, ref} from "vue";

onMounted(() => {
  ory.toSession()
      .then(({data}) => {
        session.value = data;
      })
      .catch((error) => {
        session.value = null;
      });
})

let session = ref<Session | null>(null);

function login() {
  window.location.href = getLoginLink();
}

</script>

<style scoped>

</style>
