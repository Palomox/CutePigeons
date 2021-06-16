<template>
  <div>
    <label class="text-3xl">☀</label>
    <label class="switch">
      <input @click="toggle" type="checkbox" v-if="toggled" checked>
      <input @click="toggle" type="checkbox" v-if="!toggled">
      <span class="slider round"></span>
    </label>
    <label class="text-3xl">🌙️</label>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from "vue-class-component";

@Options({
  name: "mode-toggle",
  props: {
    toggled: Boolean
  }
})
export default class ModeToggle extends Vue{
  toggled = true

  toggle(){
    this.$emit('toggle')
  }
}
</script>

<style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;

.switch{
  width: 60px;
  height: 36px;
  @apply inline-block relative
}
.switch input{
  width: 0;
  height: 0;
  opacity: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}
</style>
