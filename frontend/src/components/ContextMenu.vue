<template>
  <div v-if="open">
    <div v-if="type === 'delete'">
      <button @click="$emit('delete')" class="bg-gray-900 rounded-md text-red-800 flex flex-row gap-0.5 p-1">
        <font-awesome-icon icon="fa-regular fa-trash-can" class="self-center"/>
        <span class="self-center">Delete</span>
      </button>
    </div>
    <div v-if="type === 'modactions'">
      <button @click="$emit('mod-action', true)" class="bg-gray-900 rounded-md text-green-600 flex flex-row gap-0.5 p-1">
        <font-awesome-icon icon="fa-solid fa-check" class="self-center"/>
        <span class="self-center">Allow</span>
      </button>
      <button @click="$emit('mod-action', false)" class="bg-gray-900 rounded-md text-red-800 flex flex-row gap-0.5 p-1">
        <font-awesome-icon icon="fa-solid fa-xmark" class="self-center"/>
        <span class="self-center">Deny</span>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  open: {type: Boolean, required: true},
  type: {type: String, required: true}
});

defineEmits<{
  (e: "delete"): void,
  (e: "mod-action", allowed: boolean): void,
}>()


</script>

<style scoped>

</style>
