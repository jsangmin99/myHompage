<template>
  <div class="book window-container">
    <div class="window-header">
      <div class="window-controls">
        <router-link to="/" class="control close"></router-link>
        <span class="control minimize"></span>
        <span class="control maximize"></span>
      </div>
      <span class="window-title">독서 노트</span>
    </div>
    <div class="window-content">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="book-list">
            <h4>읽은 책</h4>
            <el-input v-model="search" placeholder="책 검색" prefix-icon="Search" clearable />
            <el-scrollbar height="500px">
              <el-menu :default-active="activeBook?.id.toString()" class="book-menu">
                <el-menu-item
                  v-for="book in filteredBooks"
                  :key="book.id"
                  :index="book.id.toString()"
                  @click="selectBook(book)"
                >
                  <el-icon><Reading /></el-icon>
                  <span>{{ book.title }}</span>
                </el-menu-item>
              </el-menu>
            </el-scrollbar>
          </div>
        </el-col>
        <el-col :span="16">
          <div v-if="activeBook" class="book-detail">
            <div class="book-header">
              <h3>{{ activeBook.title }}</h3>
              <p class="book-meta">
                <span>저자: {{ activeBook.author }}</span>
                <span>완독일: {{ activeBook.date }}</span>
              </p>
            </div>
            <div class="book-content">
              <h4>독서 노트</h4>
              <div class="note-content">
                {{ activeBook.note }}
              </div>
              <div class="book-tags">
                <el-tag v-for="tag in activeBook.tags" :key="tag" class="tag">
                  {{ tag }}
                </el-tag>
              </div>
            </div>
          </div>
          <div v-else class="empty-state">
            <el-empty description="책을 선택해주세요" />
          </div>
        </el-col>
      </el-row>

      <el-button type="primary" class="add-button" @click="showAddBookDialog"> 책 추가 </el-button>

      <el-dialog v-model="dialogVisible" title="책 추가" width="50%">
        <el-form :model="form" label-width="100px">
          <el-form-item label="제목">
            <el-input v-model="form.title" />
          </el-form-item>
          <el-form-item label="저자">
            <el-input v-model="form.author" />
          </el-form-item>
          <el-form-item label="완독일">
            <el-date-picker v-model="form.date" type="date" placeholder="날짜 선택" />
          </el-form-item>
          <el-form-item label="독서 노트">
            <el-input v-model="form.note" type="textarea" :rows="4" />
          </el-form-item>
          <el-form-item label="태그">
            <el-select v-model="form.tags" multiple filterable allow-create placeholder="태그 입력">
              <el-option v-for="tag in allTags" :key="tag" :label="tag" :value="tag" />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">취소</el-button>
            <el-button type="primary" @click="addBook">저장</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { Reading } from "@element-plus/icons-vue";

const search = ref("");
const dialogVisible = ref(false);
const activeBook = ref(null);

const form = ref({
  title: "",
  author: "",
  date: "",
  note: "",
  tags: [],
});

const books = ref([
  {
    id: 1,
    title: "Vue.js 완벽 가이드",
    author: "홍길동",
    date: "2024-04-01",
    note: "Vue.js의 기본 개념부터 고급 기능까지 자세히 설명된 책입니다. 특히 Composition API 부분이 매우 유익했습니다.",
    tags: ["프로그래밍", "웹개발", "Vue.js"],
  },
  {
    id: 2,
    title: "클린 코드",
    author: "로버트 C. 마틴",
    date: "2024-03-15",
    note: "코드 품질을 높이는 방법과 원칙들을 배울 수 있었습니다. 실제 프로젝트에 적용하면 좋을 것 같습니다.",
    tags: ["프로그래밍", "개발방법론"],
  },
]);

const allTags = computed(() => {
  const tags = new Set();
  books.value.forEach((book) => {
    book.tags.forEach((tag) => tags.add(tag));
  });
  return Array.from(tags);
});

const filteredBooks = computed(() => {
  if (!search.value) return books.value;
  const searchLower = search.value.toLowerCase();
  return books.value.filter(
    (book) =>
      book.title.toLowerCase().includes(searchLower) ||
      book.author.toLowerCase().includes(searchLower)
  );
});

const selectBook = (book) => {
  activeBook.value = book;
};

const showAddBookDialog = () => {
  form.value = {
    title: "",
    author: "",
    date: "",
    note: "",
    tags: [],
  };
  dialogVisible.value = true;
};

const addBook = () => {
  if (!form.value.title || !form.value.author || !form.value.date) {
    return;
  }

  const newBook = {
    id: books.value.length + 1,
    title: form.value.title,
    author: form.value.author,
    date: form.value.date.toISOString().split("T")[0],
    note: form.value.note,
    tags: form.value.tags,
  };

  books.value.push(newBook);
  dialogVisible.value = false;
  activeBook.value = newBook;
};
</script>

<style scoped>
.book {
  max-width: 1200px;
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

.book-list {
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
}

.book-list h4 {
  margin: 0 0 20px;
}

.book-menu {
  margin-top: 20px;
  border: none;
}

.book-detail {
  padding: 20px;
}

.book-header {
  margin-bottom: 30px;
}

.book-header h3 {
  margin: 0 0 10px;
}

.book-meta {
  color: #666;
  display: flex;
  gap: 20px;
}

.book-content h4 {
  margin: 0 0 20px;
}

.note-content {
  line-height: 1.6;
  color: #333;
  margin-bottom: 20px;
}

.book-tags {
  margin-top: 20px;
}

.tag {
  margin-right: 8px;
}

.empty-state {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-button {
  position: fixed;
  bottom: 40px;
  right: 40px;
}

.el-input {
  margin-bottom: 20px;
}
</style>
