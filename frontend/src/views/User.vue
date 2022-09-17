<template>
  <nav-bar/>
  <div class="flex flex-col items-center justify-center mt-10 gap-4">
    <form class="bg-gray-700 rounded-md p-3 flex flex-col gap-2" @submit.prevent="submitData">
      <label class="text-2xl">Display name</label>
      <input class="rounded-md p-1 text-black" v-model="adding.displayName" type="text">
      <input type="submit" class="cursor-pointer bg-blue-900 rounded-md text-xl font-medium" value="Submit">
    </form>
    <div class="bg-gray-700 rounded-md p-3 flex-auto">
      <label class="flex-auto bg-blue-900 text-white cursor-pointer p-2 mt-4 text-2xl rounded-md text-center"
             @click="logout">Logout</label>
    </div>
  </div>
  <div class="bottom-2 left-2 absolute bg-blue-900 w-16 h-16 shadow shadow-md rounded-full"
       @click="$router.push('/')">
    <font-awesome-icon class="text-white text-4xl relative transform w-full translate-y-[40%]" icon="fa-solid fa-house"/>
  </div>
</template>

<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
import {ory, userApi} from "@/store/apis";
import {ref} from "vue";

let adding = ref({
  displayName: "",
});

function submitData() {
  userApi.editUser(adding);
}

function logout() {
  ory.createSelfServiceLogoutFlowUrlForBrowsers().then(({data}) => {
    let url = data.logout_url + "&return_to=" + window.location.href;
    window.location.href = url;
  });
}
</script>

<style scoped>

</style>
