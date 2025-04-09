<template>
  <div class="blog-content">
    <h2>블로그</h2>
    <div class="posts">
      <div v-for="post in posts" :key="post.id" class="post">
        <h3>{{ post.title }}</h3>
        <p>{{ post.content }}</p>
        <span class="date">{{ post.date }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import api from "@/api";

interface Post {
  id: number;
  title: string;
  content: string;
  date: string;
}

const posts = ref<Post[]>([]);

const fetchPosts = async () => {
  try {
    const res = await api.get("/posts");
    posts.value = res.data;
  } catch (error) {
    console.error("블로그 포스트 불러오기 실패:", error);
  }
};

onMounted(() => {
  fetchPosts();
});
</script>

<style scoped>
.blog-content {
  padding: 20px;
  color: #333;
  height: 100%;
  overflow-y: auto;
}

.post {
  margin-bottom: 20px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.post h3 {
  margin-bottom: 10px;
  color: #2c3e50;
}

.post p {
  margin-bottom: 10px;
  line-height: 1.6;
}

.date {
  font-size: 0.8em;
  color: #666;
}
</style>
