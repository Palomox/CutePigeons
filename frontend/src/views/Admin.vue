<template>
  <div>
    <div class="flex flex-col" v-if="!this.$auth.loading">
      <router-link class="self-end" to="/">Watch</router-link>
      <!-- show login when not authenticated -->
      <button class="self-end" v-if="!this.$auth.isAuthenticated" @click="login">Log in</button>
      <!-- show logout when authenticated -->
      <button class="self-end" v-if="this.$auth.isAuthenticated" @click="logout">Log out</button>
    </div>
    <div class="flex flex-row gap-x-4 justify-center" v-if="this.$auth.isAuthenticated && this.mayAdministrate">
      <form class="self-center flex flex-col w-1/3 border border-gray-500 font-bold rounded-md border-4 p-1" @submit.prevent="addPigeon">
        <label class="self-center text-xl">New Pigeon</label>
        <label>Pigeon url</label>
        <input class="text-black" @focus="resetAdding" type="url" v-model="pigeonAdded.url">
        <button type="submit">Submit</button>
        <div v-if="successAdding" class="text-center rounded-md bg-green-500 text-black">
          <span>Pigeon added successfully!</span>
          <span>Added pigeon with id {{pigeonAddedId}}</span>
        </div>
        <div v-if="errorAdding.present" class="text-center rounded-md bg-red-500 text-black">
          <span>Error adding pigeon</span>
          <span v-text="errorAdding.code"></span>
        </div>
      </form>
      <form class=" flex flex-col w-1/3 border border-gray-500 font-bold rounded-md border-4 p-1" @submit.prevent="removePigeon">
        <label class="self-center text-xl">Delete Pigeon</label>
        <label>Pigeon id</label>
        <input class="text-black" type="number" @focus="resetRemoving" v-model="pigeonDeleted.id">
        <button type="submit">Submit</button>
        <div v-if="successRemoving" class="text-center rounded-md bg-green-500 text-black">
          <span>Pigeon removed successfully!</span>
          <span>Removed pigeon with id {{pigeonDeleted.id}}</span>
        </div>
        <div v-if="errorRemoving.present" class="text-center rounded-md bg-red-500 text-black">
          <span>Error removing pigeon</span>
          <span v-text="errorRemoving.code"></span>
        </div>
      </form>
    </div>
    <button @click="mayAdministrate()">test</button>
    </div>
</template>
<script lang="ts">
import {Options, Vue} from "vue-class-component";
import {inject} from "vue";
import {V0alpha2Api} from "@ory/kratos-client";

@Options({
  name: 'admin',
})
export default class Admin extends Vue{
  $auth : any;
  $kratos ?: V0alpha2Api;

  beforeCreate(){
    this.$kratos = inject('$kratos')
    this.$auth = inject('$auth')
  }

  successAdding = false
  successRemoving = false
  errorAdding : any = {
    present: false,
    code: ""
  }
  errorRemoving: any = {
    present: false,
    code: ""
  }
  pigeonAddedId ?: number = 0
  pigeonDeleted : any = {
    id: undefined
  }
  pigeonAdded : any = {
    url: ""
  }
  mayAdministrate(){
    let token = this.$auth.auth0Client.getTokenSilently({
      scope: "write:pigeons"
    })

    return true
  }
  resetAdding(){
    this.successAdding = false
    this.errorAdding = {
      present: false,
      code: ""
    }
    this.pigeonAddedId = undefined
  }
  resetRemoving(){
    this.successRemoving = false
    this.errorRemoving = {
      present: false,
      code: ""
    }
  }
  login(){
    /*
    this.$auth.auth0Client.loginWithRedirect({
      redirectUri: window.history.back()
    })
    console.info(this.$auth)
    */

  }
  api = "https://cutepigeons.palomox.ga/api/v1/admin/"
  async addPigeon(){
    const token = await this.$auth.auth0Client.getTokenSilently({
      scope: "write:pigeons"
    })


    const response = await fetch(this.api+"addPigeon", {
      mode: "cors",
      method: "POST",
      body: JSON.stringify(this.pigeonAdded),
      headers: {
        Authorization: "Bearer "+token
      }
    })

    const json = await response.json()

    switch (json.status.toLocaleLowerCase()){
      case "success":
        this.successAdding = true
        this.pigeonAddedId = json.id
        break;
      case "error":
        this.errorAdding = {
          present: true,
          code: json.error
        }
        break;
      default:
        break;
    }
  }
  async removePigeon(){
    const token = await this.$auth.auth0Client.getTokenSilently()


    const response = await fetch(this.api+"deletePigeon", {
      mode: "cors",
      method: "DELETE",
      body: JSON.stringify(this.pigeonDeleted),
      headers: {
        Authorization: "Bearer "+token
      }
    })

    const json = await response.json()

    switch (json.status.toLocaleLowerCase()){
      case "success":
        this.successRemoving = true
        break;
      case "error":
        this.successRemoving = false
        this.errorRemoving = {
          present: true,
          code: json.error
        }
        break;
      default:
        break;
    }
  }
  logout(){
    console.info(this.$auth)
    this.$auth.auth0Client.logout({
      returnTo: window.location.origin
    })
  }
}
</script>
<style>
@tailwind base;
@tailwind components;
@tailwind utilities;
</style>
