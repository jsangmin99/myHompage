<template>
  <div class="login-form">
    <h2>로그인</h2>
    <form @submit.prevent="login">
      <input v-model="username" placeholder="Username" required />
      <input v-model="password" type="password" placeholder="Password" required />
      <button type="submit">로그인</button>
      <p v-if="error" class="error">{{ error }}</p>
    </form>

    <!-- ✅ 회원가입 링크 추가 -->
    <div class="signup-link">
      계정이 없으신가요?
      <span @click="openSignup">회원가입</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import axios from "axios";
import { useAuthStore } from "@/stores/auth";

const authStore = useAuthStore();

// ✅ 상태
const username = ref("");
const password = ref("");
const error = ref("");

// ✅ 로그인 요청
const login = async () => {
  try {
    const response = await axios.post("http://localhost:8080/api/auth/login", {
      username: username.value,
      password: password.value,
    });

    const { accessToken, refreshToken } = response.data;

    authStore.login(accessToken, refreshToken); // ✅ Pinia를 통한 로그인 처리

    error.value = "";
    alert("로그인 성공!");
  } catch (err: any) {
    error.value = "로그인 실패. 아이디/비밀번호를 확인하세요.";
  }
};

// ✅ 회원가입 창 열기
const openSignup = () => {
  const menu = {
    name: "signup",
    title: "회원가입",
  };
  const event = new CustomEvent("open-menu", { detail: menu });
  window.dispatchEvent(event);
};
</script>

<style scoped>
.login-form {
  padding: 20px;
  background: white;
  border-radius: 10px;
}
input {
  display: block;
  margin-bottom: 10px;
  padding: 8px;
  width: 100%;
}
button {
  padding: 8px 16px;
}
.error {
  color: red;
  margin-top: 10px;
}

/* ✅ 회원가입 링크 스타일 */
.signup-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}
.signup-link span {
  color: #007bff;
  cursor: pointer;
  text-decoration: underline;
}
.signup-link span:hover {
  color: #0056b3;
}
</style>
