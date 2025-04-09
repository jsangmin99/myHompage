<template>
  <div class="app">
    <ThreeScene />
    <router-view />
  </div>
</template>

<script setup lang="ts">
import ThreeScene from "./components/common/ThreeScene.vue";
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.app {
  position: relative;
  width: 100%;
  height: 100vh;
  background: #f0f0f0;
  overflow: hidden;
}
</style>
