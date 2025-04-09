<template>
  <div class="about">
    <el-row :gutter="20">
      <el-col :span="16" :offset="4">
        <el-card class="about-card">
          <template #header>
            <div class="card-header">
              <h2>블로그 소개</h2>
            </div>
          </template>
          <div class="about-content">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="about-image">
                  <el-avatar
                    :size="200"
                    src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                  />
                </div>
              </el-col>
              <el-col :span="16">
                <div class="about-text">
                  <h3>안녕하세요!</h3>
                  <p>이 블로그는 Vue.js와 Spring Boot를 사용하여 개발된 개인 블로그입니다.</p>
                  <p>주로 웹 개발, 프로그래밍, 기술 관련 내용을 다루고 있습니다.</p>
                  <p>여러분의 피드백과 의견은 언제나 환영합니다!</p>

                  <div class="contact-info">
                    <h4>연락처</h4>
                    <p>
                      <el-icon><Message /></el-icon> email@example.com
                    </p>
                    <p>
                      <el-icon><Link /></el-icon> github.com/username
                    </p>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card class="skills-card">
          <template #header>
            <div class="card-header">
              <h3>기술 스택</h3>
            </div>
          </template>
          <div class="skills-content">
            <el-row :gutter="20">
              <el-col :span="8" v-for="skill in skills" :key="skill.name">
                <el-card shadow="hover" class="skill-card">
                  <el-icon :size="40" :color="skill.color"><component :is="skill.icon" /></el-icon>
                  <h4>{{ skill.name }}</h4>
                  <p>{{ skill.description }}</p>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { Message, Link, Monitor, Connection, DataLine } from "@element-plus/icons-vue";

const skills = [
  {
    name: "Vue.js",
    icon: Monitor,
    color: "#42b883",
    description: "프론트엔드 개발",
  },
  {
    name: "Spring Boot",
    icon: Connection,
    color: "#6DB33F",
    description: "백엔드 개발",
  },
  {
    name: "Database",
    icon: DataLine,
    color: "#00758F",
    description: "데이터베이스 관리",
  },
];
</script>

<style scoped>
.about {
  padding: 20px 0;
}

.about-card {
  margin-bottom: 30px;
}

.card-header {
  text-align: center;
}

.about-content {
  padding: 20px;
}

.about-image {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.about-text {
  padding: 20px;
}

.about-text h3 {
  margin-bottom: 20px;
}

.about-text p {
  margin-bottom: 10px;
  line-height: 1.6;
}

.contact-info {
  margin-top: 30px;
}

.contact-info h4 {
  margin-bottom: 15px;
}

.contact-info p {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.skills-card {
  margin-top: 30px;
}

.skills-content {
  padding: 20px;
}

.skill-card {
  text-align: center;
  padding: 20px;
  margin-bottom: 20px;
}

.skill-card h4 {
  margin: 15px 0 10px;
}

.skill-card p {
  color: #666;
  line-height: 1.6;
}
</style>
