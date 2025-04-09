<template>
  <div class="music window-container">
    <div class="window-header">
      <div class="window-controls">
        <router-link to="/" class="control close"></router-link>
        <span class="control minimize"></span>
        <span class="control maximize"></span>
      </div>
      <span class="window-title">뮤직 플레이어</span>
    </div>
    <div class="window-content">
      <div class="player">
        <div class="album-cover">
          <el-image :src="currentTrack.cover" fit="cover" />
        </div>
        <div class="track-info">
          <h3>{{ currentTrack.title }}</h3>
          <p>{{ currentTrack.artist }}</p>
        </div>
        <div class="controls">
          <el-button-group>
            <el-button :icon="ArrowLeft" circle @click="previousTrack" />
            <el-button :icon="playing ? VideoPause : VideoPlay" circle @click="togglePlay" />
            <el-button :icon="ArrowRight" circle @click="nextTrack" />
          </el-button-group>
        </div>
        <div class="progress">
          <el-slider v-model="progress" :show-tooltip="false" />
          <div class="time">
            <span>{{ formatTime(currentTime) }}</span>
            <span>{{ formatTime(duration) }}</span>
          </div>
        </div>
      </div>

      <div class="playlist">
        <h4>재생 목록</h4>
        <el-table :data="playlist" style="width: 100%">
          <el-table-column prop="title" label="제목" />
          <el-table-column prop="artist" label="아티스트" />
          <el-table-column prop="duration" label="시간" width="100" />
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { ArrowLeft, ArrowRight, VideoPlay, VideoPause } from "@element-plus/icons-vue";

const playing = ref(false);
const progress = ref(0);
const currentTime = ref(0);
const duration = ref(180);

const currentTrack = ref({
  title: "Sample Track",
  artist: "Sample Artist",
  cover: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
});

const playlist = ref([
  {
    title: "Sample Track 1",
    artist: "Artist 1",
    duration: "3:00",
  },
  {
    title: "Sample Track 2",
    artist: "Artist 2",
    duration: "4:15",
  },
  {
    title: "Sample Track 3",
    artist: "Artist 3",
    duration: "3:45",
  },
]);

const togglePlay = () => {
  playing.value = !playing.value;
};

const previousTrack = () => {
  // TODO: 이전 트랙 재생
};

const nextTrack = () => {
  // TODO: 다음 트랙 재생
};

const formatTime = (seconds) => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = Math.floor(seconds % 60);
  return minutes + ":" + remainingSeconds.toString().padStart(2, "0");
};
</script>

<style scoped>
.music {
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

.player {
  text-align: center;
  margin-bottom: 40px;
}

.album-cover {
  width: 200px;
  height: 200px;
  margin: 0 auto 20px;
  border-radius: 8px;
  overflow: hidden;
}

.track-info {
  margin-bottom: 20px;
}

.track-info h3 {
  margin: 0 0 5px;
  color: #333;
}

.track-info p {
  margin: 0;
  color: #666;
}

.controls {
  margin-bottom: 20px;
}

.progress {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
}

.time {
  display: flex;
  justify-content: space-between;
  color: #666;
  font-size: 12px;
  margin-top: 5px;
}

.playlist {
  margin-top: 40px;
}

.playlist h4 {
  margin: 0 0 20px;
  color: #333;
}
</style>
