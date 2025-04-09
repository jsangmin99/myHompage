<template>
  <iframe :src="url" class="external-iframe" frameborder="0" />
</template>

<script setup lang="ts">
defineProps<{ url: string }>();
</script>

<style scoped>
.external-iframe {
  width: 100%;
  height: 100%;
  border: none;
}
</style>
