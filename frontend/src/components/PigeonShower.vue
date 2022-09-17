<template>
<div v-if="!error" class="flex flex-col">
  <img class="self-center w-1/3 h-auto border-gray-500 border-solid border border-4 rounded-md p-1" :src="pigeon.url">
  <span class="self-center">Pigeon number {{pigeon.id}}.</span>
  <div class="self-center">
    <button class="bg-blue-500 rounded-md p-1" @click="swapPigeon">Get pigeon!</button>
    <a class="ml-1 text-blue-700 hover:text-purple-700" href="https://cutepigeons.palomox.ga/api/v1">Use the API</a>
  </div>
</div>
  <div v-if="error" class="flex flex-col">
    <popup class="self-center" msg="Pigeon not found" bgColor="red"/>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from "vue-class-component";
import {Pigeon} from "../Types";
import Popup from "@/components/Popup.vue";

@Options({
  name: "pigeon-shower",
  props: {
    pigeon: Object,
    pigeonId: Number
  },
  components: {
    Popup
  }
})
export default class PigeonShower extends Vue{
  pigeon: Pigeon = {
    id: 0,
    url: ''
  }
  error = true
  pigeonId ?:number
  mounted(){
    window.addEventListener('keydown', ev => {
      if(ev.key == 'Enter') {
        this.swapPigeon()
      }
    })
    if(this.pigeonId != undefined){
      this.swapPigeonToId(this.pigeonId)
    }else{
      this.swapPigeon()
    }
  }
  api = "https://cutepigeons.palomox.ga/api/v1/"
  async swapPigeon(){
    const data = await fetch(this.api+'public/pigeons', {
      mode: "cors",
      method: "GET"
    })
    if(!data.ok){
      this.error = true
      return
    }
    this.error = false
    const responseBody = await data.json()
    const pigeonsLength = responseBody.pigeons.length

    const pigeonId = this.randomIntFromInterval(1, pigeonsLength)

    const chosenPigeon = responseBody.pigeons[pigeonId]

    this.$emit('change-pigeon', chosenPigeon)
    await this.$router.push('/?id='+chosenPigeon.id)
  }
  async swapPigeonToId(id:number){
    const data = await fetch(this.api+'public/pigeon/'+id, {
      mode: "cors",
      method: "GET"
    })
    if(!data.ok){
      this.error = true;
      return
    }
    this.error = false;
    const responseBody = await data.json()

    const chosenPigeon = responseBody

    this.$emit('change-pigeon', chosenPigeon)
  }
  /**
   * Generates random number between min and max, both included
   * @param min Minimum number to generate between (included)
   * @param max Maximum number to generate between (included)
   */
  randomIntFromInterval(min:number, max:number) : number {
    return Math.floor(Math.random() * (max - min + 1) + min)
  }
}
</script>

<style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;

</style>
