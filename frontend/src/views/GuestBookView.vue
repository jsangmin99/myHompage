<template>
  <div class="guestbook window-container">
    <div class="window-header">
      <div class="window-controls">
        <router-link to="/" class="control close"></router-link>
        <span class="control minimize"></span>
        <span class="control maximize"></span>
      </div>
      <span class="window-title">방명록</span>
    </div>
    <div class="window-content">
      <el-form :model="form" class="message-form">
        <el-form-item>
          <el-input v-model="form.name" placeholder="이름" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.message" type="textarea" placeholder="메시지를 남겨주세요" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitMessage">등록</el-button>
        </el-form-item>
      </el-form>

      <div class="messages">
        <el-timeline>
          <el-timeline-item
            v-for="message in messages"
            :key="message.id"
            :timestamp="message.date"
            placement="top"
          >
            <el-card>
              <h4>{{ message.name }}</h4>
              <p>{{ message.message }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const form = ref({
  name: "",
  message: "",
});

const messages = ref([
  {
    id: 1,
    name: "홍길동",
    message: "멋진 블로그네요!",
    date: "2024-04-08",
  },
  {
    id: 2,
    name: "김철수",
    message: "자주 놀러오겠습니다~",
    date: "2024-04-07",
  },
]);

const submitMessage = () => {
  if (!form.value.name || !form.value.message) {
    return;
  }

  messages.value.unshift({
    id: messages.value.length + 1,
    name: form.value.name,
    message: form.value.message,
    date: new Date().toISOString().split("T")[0],
  });

  form.value.name = "";
  form.value.message = "";
};
</script>

<style scoped>
.guestbook {
  max-width: 800px;
  margin: 20px auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.window-container {
  overflow: hidden;
}

.window-header {
  display: flex;
  align-items: center;
  padding: 8px;
  background-color: #f5f5f5;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.window-controls {
  display: flex;
  gap: 6px;
  margin-right: 10px;
}

.control {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  display: inline-block;
}

.close {
  background-color: #ff5f56;
}

.minimize {
  background-color: #ffbd2e;
}

.maximize {
  background-color: #27c93f;
}

.window-title {
  font-size: 14px;
  color: #666;
}

.window-content {
  padding: 20px;
}

.message-form {
  margin-bottom: 30px;
}

.messages {
  margin-top: 20px;
}

.el-timeline-item {
  margin-bottom: 20px;
}

.el-card {
  margin-top: 10px;
}

h4 {
  margin: 0 0 10px;
  color: var(--el-color-primary);
}

p {
  margin: 0;
  color: #666;
}
</style>
