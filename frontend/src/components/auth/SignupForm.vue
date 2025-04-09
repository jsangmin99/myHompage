<template>
  <div class="signup-form">
    <h2>회원가입</h2>
    <form @submit.prevent="signup">
      <input v-model="username" placeholder="Username" required />
      <input v-model="email" type="email" placeholder="Email" required />
      <input v-model="password" type="password" placeholder="Password" required />
      <button type="submit">회원가입</button>
      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="success" class="success">{{ success }}</p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import axios from "axios";

const username = ref("");
const email = ref("");
const password = ref("");
const error = ref("");
const success = ref("");

const signup = async () => {
  try {
    const response = await axios.post("http://localhost:8080/api/auth/signup", {
      username: username.value,
      email: email.value,
      password: password.value,
    });

    success.value = response.data;
    error.value = "";
  } catch (err: any) {
    error.value = err.response?.data || "회원가입 실패";
    success.value = "";
  }
};
</script>

<style scoped>
.signup-form {
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
}
.success {
  color: green;
}
</style>
