<template>
  <div>
    <div class="flex flex-col">
      <router-link class="self-end" to="/">Watch</router-link>
      <!-- show login when not authenticated -->
      <a class="self-end" v-if="!session" :href="basePath + '/ui/login?return_to='+this.url">Log in</a>
      <!-- show logout and settings when authenticated -->
      <a class="self-end" v-if="session" :href="logoutUrl">Log out</a>
      <a class="self-end" v-if="session" :href="basePath + '/ui/settings'">Settings</a>
      <button @click="userInfoBackend()">aasas</button>

    </div>
    <div class="flex flex-row gap-x-4 justify-center" v-if="session">
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
    </div>
</template>
<script lang="ts">
import {Options, Vue} from "vue-class-component";
import {V0alpha2Api, Configuration, Session, ReadApi} from "@ory/client";

const basePath = process.env.VUE_APP_ORY_URL || "http://localhost:4000"
const ory = new V0alpha2Api(
    new Configuration({
      basePath,
      baseOptions: {
        // Ensures we send cookies in the CORS requests.
        withCredentials: true,
      },
    }),
)

@Options({
  name: 'admin',
})
export default class Admin extends Vue{
  basePath = basePath;
  logoutUrl :string|null = null;
  session : Session|null = null;
  url = "";

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

  mounted(){
    ory.toSession().then(({data}) => {
      this.session = data;

      ory.createSelfServiceLogoutFlowUrlForBrowsers().then(({data}) => {
        this.logoutUrl = data.logout_url;
      })
    })
    this.url = window.location.href
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

  //api = "https://cutepigeons.palomox.ga/api/v1/admin/"
  api = "http://localhost:5555/api/v1/"
  userInfoBackend(){
    fetch(this.api+"authenticated/userDetails", {
      credentials: "include"
    }).then((res) =>{
      if(res.ok){
        console.info(res.json())
      }
    })
  }


  async addPigeon(){
    const response = await fetch(this.api+"admin/addPigeon", {
      mode: "cors",
      method: "POST",
      credentials: "include",
      body: JSON.stringify(this.pigeonAdded),
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
    const response = await fetch(this.api+"admin/deletePigeon", {
      mode: "cors",
      method: "DELETE",
      credentials: "include",
      body: JSON.stringify(this.pigeonDeleted),
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
}
</script>
<style>
@tailwind base;
@tailwind components;
@tailwind utilities;
</style>
