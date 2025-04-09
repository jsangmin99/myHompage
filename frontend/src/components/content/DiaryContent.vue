<template>
  <div class="diary-content">
    <h2>다이어리</h2>
    <div class="diary-entries">
      <div v-for="entry in entries" :key="entry.id" class="entry">
        <h3>{{ entry.title }}</h3>
        <p>{{ entry.content }}</p>
        <span class="date">{{ entry.date }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const entries = ref([
  {
    id: 1,
    title: "오늘의 일기",
    content: "오늘은 정말 즐거운 하루였습니다. 새로운 프로젝트를 시작했어요!",
    date: "2024-03-20",
  },
  {
    id: 2,
    title: "주말 일기",
    content: "주말에는 친구들과 함께 공원에 갔습니다. 날씨가 정말 좋았어요.",
    date: "2024-03-21",
  },
]);
</script>

<style scoped>
.diary-content {
  padding: 20px;
  color: #333;
  height: 100%;
  overflow-y: auto;
}

.entry {
  margin-bottom: 20px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.entry h3 {
  margin-bottom: 10px;
  color: #2c3e50;
}

.entry p {
  margin-bottom: 10px;
  line-height: 1.6;
}

.date {
  font-size: 0.8em;
  color: #666;
}
</style>
