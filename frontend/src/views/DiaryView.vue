<template>
  <div class="diary window-container">
    <div class="window-header">
      <div class="window-controls">
        <router-link to="/" class="control close"></router-link>
        <span class="control minimize"></span>
        <span class="control maximize"></span>
      </div>
      <span class="window-title">다이어리</span>
    </div>
    <div class="window-content">
      <el-calendar v-model="currentDate">
        <template #dateCell="{ data }">
          <div class="calendar-cell">
            <p :class="{ 'is-selected': isSelected(data.day) }">
              {{ data.day.split("-").slice(2).join("") }}
              {{ getDiaryEntry(data.day) }}
            </p>
          </div>
        </template>
      </el-calendar>

      <el-dialog v-model="dialogVisible" title="일기 작성" width="50%">
        <el-form :model="form">
          <el-form-item label="날짜">
            <el-date-picker v-model="form.date" type="date" placeholder="날짜 선택" />
          </el-form-item>
          <el-form-item label="내용">
            <el-input
              v-model="form.content"
              type="textarea"
              :rows="4"
              placeholder="오늘의 일기를 작성해주세요"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">취소</el-button>
            <el-button type="primary" @click="saveDiary">저장</el-button>
          </span>
        </template>
      </el-dialog>

      <el-button type="primary" class="add-button" @click="showDialog"> 일기 작성 </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const currentDate = ref(new Date());
const dialogVisible = ref(false);
const form = ref({
  date: "",
  content: "",
});

const diaryEntries = ref([
  {
    date: "2024-04-08",
    content: "오늘은 블로그를 만들었다",
  },
  {
    date: "2024-04-07",
    content: "Vue.js 공부를 했다",
  },
]);

const isSelected = (day) => {
  return diaryEntries.value.some((entry) => entry.date === day);
};

const getDiaryEntry = (day) => {
  const entry = diaryEntries.value.find((entry) => entry.date === day);
  return entry ? "📝" : "";
};

const showDialog = () => {
  form.value.date = new Date();
  form.value.content = "";
  dialogVisible.value = true;
};

const saveDiary = () => {
  if (!form.value.date || !form.value.content) {
    return;
  }

  const date = form.value.date.toISOString().split("T")[0];
  diaryEntries.value.push({
    date,
    content: form.value.content,
  });

  dialogVisible.value = false;
};
</script>

<style scoped>
.diary {
  max-width: 1000px;
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
  position: relative;
}

.calendar-cell {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.calendar-cell p {
  margin: 0;
  height: 24px;
  width: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.is-selected {
  color: var(--el-color-primary);
  font-weight: bold;
}

.add-button {
  position: fixed;
  bottom: 40px;
  right: 40px;
}

.dialog-footer {
  margin-top: 20px;
  text-align: right;
}
</style>
