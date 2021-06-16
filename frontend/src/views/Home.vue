<template>
  <div class="home flex flex-col">
    <popup class="mb-1 self-center" bgColor="blue" msg="Press enter or 'Get pigeon' to show a random pigeon!"></popup>
    <pigeon-shower @change-pigeon="changePigeon" :pigeonId="pigeonId" :pigeon="pigeon"></pigeon-shower>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import Popup from '@/components/Popup.vue';
import PigeonShower from '@/components/PigeonShower.vue'
import {Pigeon} from "@/Types";

@Options({
  components: {
    Popup,
    PigeonShower
  },
  props: {
    pigeonId : Number
  }
})
export default class Home extends Vue {
  pigeon: Pigeon = {
    id: 0,
    url: ""
  }
  pigeonId ?:number

  changePigeon(pigeon : Pigeon){
    this.pigeon = pigeon
  }
}
</script>
<style>
@tailwind base;
@tailwind components;
@tailwind utilities;
html{
  @apply dark:bg-gray-800 dark:text-white bg-white text-black font-sans ;
}
</style>
