<template>
  <div class="flex flex-col items-center">
    <span class="text-sm text-gray-400">{{ label }}</span>
    <span class="text-xl font-bold text-white">{{ value }}</span>
  </div>
</template>

<script setup lang="ts">
defineProps({
  label: {
    type: String,
    required: true,
  },
  value: {
    type: [String, Number],
    required: true,
  },
})
</script>
