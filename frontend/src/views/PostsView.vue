<template>
  <div class="posts">
    <el-row :gutter="20">
      <el-col :span="16" :offset="4">
        <div class="posts-header">
          <h2>블로그 포스트</h2>
          <el-button type="primary" @click="createPost">새 포스트 작성</el-button>
        </div>

        <el-tabs v-model="activeTab" class="posts-tabs">
          <el-tab-pane label="최신 포스트" name="latest">
            <el-card v-for="post in posts" :key="post.id" class="post-card">
              <template #header>
                <div class="post-header">
                  <h3>{{ post.title }}</h3>
                  <div class="post-meta">
                    <span
                      ><el-icon><User /></el-icon> {{ post.author }}</span
                    >
                    <span
                      ><el-icon><Calendar /></el-icon> {{ post.date }}</span
                    >
                    <span
                      ><el-icon><View /></el-icon> {{ post.views }}</span
                    >
                  </div>
                </div>
              </template>
              <div class="post-content">
                <p>{{ post.summary }}</p>
              </div>
              <div class="post-footer">
                <el-tag v-for="tag in post.tags" :key="tag" class="post-tag">{{ tag }}</el-tag>
                <el-button type="text" @click="readMore(post.id)">더보기</el-button>
              </div>
            </el-card>
          </el-tab-pane>
          <el-tab-pane label="인기 포스트" name="popular">
            <p>인기 포스트 목록이 여기에 표시됩니다.</p>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { User, Calendar, View } from "@element-plus/icons-vue";
import api from "@/api";

const activeTab = ref("latest");
const posts = ref([]);

const fetchPosts = async () => {
  try {
    const res = await api.get("/posts");
    posts.value = res.data;
  } catch (err) {
    console.error("포스트 불러오기 실패:", err);
  }
};

const createPost = () => {
  console.log("Create new post");
};

const readMore = (postId) => {
  console.log("Read more about post:", postId);
};

onMounted(() => {
  fetchPosts();
});
</script>

<style scoped>
.posts {
  padding: 20px 0;
}

.posts-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.posts-tabs {
  margin-top: 20px;
}

.post-card {
  margin-bottom: 20px;
}

.post-header {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.post-meta {
  display: flex;
  gap: 20px;
  color: #666;
  font-size: 0.9em;
}

.post-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.post-content {
  margin: 15px 0;
  line-height: 1.6;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-tag {
  margin-right: 5px;
}
</style>
