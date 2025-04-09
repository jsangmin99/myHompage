<template>
  <div class="guestbook-content">
    <h2>방명록</h2>
    <div class="guestbook-form">
      <input v-model="newMessage.name" placeholder="이름" />
      <textarea v-model="newMessage.content" placeholder="메시지를 남겨주세요"></textarea>
      <button @click="addMessage">메시지 남기기</button>
    </div>
    <div class="messages">
      <div v-for="message in messages" :key="message.id" class="message">
        <h3>{{ message.name }}</h3>
        <p>{{ message.content }}</p>
        <span class="date">{{ message.date }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const messages = ref([
  {
    id: 1,
    name: "홍길동",
    content: "안녕하세요! 방문했습니다.",
    date: "2024-03-20",
  },
  {
    id: 2,
    name: "김철수",
    content: "좋은 블로그네요!",
    date: "2024-03-21",
  },
]);

const newMessage = ref({
  name: "",
  content: "",
});

const addMessage = () => {
  if (newMessage.value.name && newMessage.value.content) {
    messages.value.push({
      id: messages.value.length + 1,
      name: newMessage.value.name,
      content: newMessage.value.content,
      date: new Date().toISOString().split("T")[0],
    });
    newMessage.value.name = "";
    newMessage.value.content = "";
  }
};
</script>

<style scoped>
.guestbook-content {
  padding: 20px;
  color: #333;
  height: 100%;
  overflow-y: auto;
}

.guestbook-form {
  margin-bottom: 20px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.guestbook-form input,
.guestbook-form textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.guestbook-form textarea {
  height: 100px;
  resize: vertical;
}

.guestbook-form button {
  padding: 10px 20px;
  background: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.message {
  margin-bottom: 20px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.message h3 {
  margin-bottom: 10px;
  color: #2c3e50;
}

.message p {
  margin-bottom: 10px;
  line-height: 1.6;
}

.date {
  font-size: 0.8em;
  color: #666;
}
</style>
