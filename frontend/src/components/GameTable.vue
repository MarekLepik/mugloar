<template>
  <div class="bg-gray-800 rounded-2xl shadow overflow-hidden">
    <div class="px-4 py-3 border-b border-gray-700">
      <h2 class="text-lg font-semibold">{{ title }}</h2>
    </div>

    <div class="max-h-120 overflow-y-auto">
      <table class="w-full text-left">
        <thead class="bg-gray-700 text-gray-300 text-sm sticky top-0 z-10">
          <tr>
            <th v-for="col in columns" :key="col" class="px-4 py-2 text-xs">
              {{ col }}
            </th>
          </tr>
        </thead>
        <tbody>
          <slot />
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  title: {
    type: String,
    required: true,
  },
  columns: {
    type: Array as () => string[],
    required: true,
  },
})
</script>
