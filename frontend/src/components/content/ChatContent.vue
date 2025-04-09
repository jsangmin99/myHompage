<template>
  <div class="chat-container">
    <div class="connection-status" :class="{ connected: isConnected }">
      {{ isConnected ? "연결됨" : "연결 중..." }}
    </div>
    <div class="chat-messages" ref="messagesContainer">
      <div
        v-for="message in messages"
        :key="message.id"
        class="message"
        :class="{ 'own-message': message.isOwn }"
      >
        <div class="message-header">
          <span class="username">{{ message.ip || "익명" }}</span>
          <span class="timestamp">{{ formatTime(message.timestamp) }}</span>
        </div>
        <div class="message-content">{{ message.content }}</div>
      </div>
    </div>
    <div class="chat-input">
      <input
        v-model="newMessage"
        @keyup.enter="sendMessage"
        placeholder="메시지를 입력하세요..."
        :disabled="!isConnected"
      />
      <button @click="sendMessage" :disabled="!isConnected || !newMessage.trim()">전송</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from "vue";

interface Message {
  ip?: string;
  content: string;
  timestamp: string;
  isOwn?: boolean;
}

const messages = ref<Message[]>([]);
const newMessage = ref("");
const isConnected = ref(false);
const ws = ref<WebSocket | null>(null);
const messagesContainer = ref<HTMLElement | null>(null);
const myIp = ref<string>("");

const formatTime = (timestamp: string): string => new Date(timestamp).toLocaleTimeString();

const scrollToBottom = async () => {
  await nextTick();
  messagesContainer.value!.scrollTop = messagesContainer.value!.scrollHeight;
};

const sendMessage = () => {
  if (!newMessage.value.trim() || !isConnected.value || !ws.value) return;

  const message = {
    type: "chat",
    content: newMessage.value,
    timestamp: new Date().toISOString(),
    user: myIp.value || "익명",
  };

  ws.value.send(JSON.stringify(message));
  newMessage.value = "";
};

const connectWebSocket = () => {
  const wsUrl = process.env.VUE_APP_WS_URL + "/ws/chat";
  console.log("WebSocket 연결 시도:", wsUrl);

  ws.value = new WebSocket(wsUrl);

  ws.value.onopen = () => {
    isConnected.value = true;
    console.log("WebSocket 연결됨");

    messages.value.push({
      content: "채팅 서버에 연결되었습니다.",
      timestamp: new Date().toISOString(),
      isOwn: false,
    });
  };

  ws.value.onmessage = (event) => {
    const message = JSON.parse(event.data);

    if (!myIp.value && message.ip) {
      myIp.value = normalizeIp(message.ip);
    }

    const incomingIp = normalizeIp(message.ip);
    const mine = incomingIp === myIp.value;

    messages.value.push({
      ...message,
      ip: message.ip,
      isOwn: mine,
    });

    scrollToBottom();
  };

  const normalizeIp = (ip: string) => {
    if (!ip) return "";
    return ip === "::1" ? "127.0.0.1" : ip;
  };

  ws.value.onclose = (event) => {
    isConnected.value = false;
    console.log("WebSocket 연결 끊김:", event.code, event.reason);

    messages.value.push({
      content: "서버와의 연결이 끊어졌습니다. 재연결 중...",
      timestamp: new Date().toISOString(),
      isOwn: false,
    });

    setTimeout(connectWebSocket, 3000);
  };

  ws.value.onerror = (event) => {
    console.error("WebSocket 에러:", event);
  };
};

onMounted(() => {
  connectWebSocket();
});

onUnmounted(() => {
  ws.value?.close();
});
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f5f5f5;
}

.connection-status {
  padding: 8px;
  text-align: center;
  background: #ff4444;
  color: white;
  font-size: 14px;
}

.connection-status.connected {
  background: #00c851;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.message {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 15px;
  background: white;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.own-message {
  align-self: flex-end;
  background: #007aff;
  color: white;
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  font-size: 12px;
}

.own-message .message-header {
  color: rgba(255, 255, 255, 0.8);
}

.username {
  font-weight: bold;
}

.timestamp {
  color: #666;
}

.own-message .timestamp {
  color: rgba(255, 255, 255, 0.8);
}

.message-content {
  word-break: break-word;
}

.chat-input {
  display: flex;
  padding: 15px;
  background: white;
  border-top: 1px solid #e0e0e0;
}

.chat-input input {
  flex: 1;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  margin-right: 10px;
  outline: none;
}

.chat-input input:focus {
  border-color: #007aff;
}

.chat-input button {
  padding: 10px 20px;
  background: #007aff;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background 0.2s;
}

.chat-input button:hover {
  background: #0056b3;
}

.chat-input button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>
