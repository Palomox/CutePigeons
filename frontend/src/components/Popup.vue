<template>
  <transition name="fade" type="">
    <span v-if="open" :style="'background: '+bgColor+';'" class="popup">
      <span class="self-end hover:underline hover:cursor-pointer" @click="dismiss">Dismiss</span>
      <span v-text="msg" class="justify-self-center text-center"></span>
    </span>
  </transition>
</template>
<script lang="ts">
import { Options, Vue } from 'vue-class-component';

@Options({
  props: {
    msg: String,
    bgColor: String,
  },
  name: "popup"
})
export default class Popup extends Vue {
  msg!: string
  bgColor !: string
  open = true

  dismiss(){
    this.open = false
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;

.popup{
  @apply rounded-md font-mono p-1 flex flex-col w-1/2 text-black visible;
  animation-fill-mode: forwards;
}
@keyframes vanish {
  from{
    opacity: 1;
  }
  to{
    opacity: 0;
    visibility: hidden;
  }
}
.fade-leave-active {
  animation: vanish 1s;
}

.fade-leave-to {
  opacity: 0;
}
</style>
