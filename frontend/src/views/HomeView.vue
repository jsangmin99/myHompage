<template>
  <div class="home">
    <div class="dock-container">
      <div class="dock">
        <div
          v-for="menu in menus"
          :key="menu.name"
          class="dock-item"
          @click="openWindow(menu)"
          @mouseover="handleMouseOver"
          @mouseout="handleMouseOut"
        >
          <img :src="menu.icon" alt="" class="dock-icon" />
          <div class="dock-tooltip">{{ menu.title }}</div>
        </div>
      </div>
    </div>
    <MacWindow
      v-for="window in activeWindows"
      :key="window.id"
      :title="window.title"
      :initial-position="window.position"
      @close="closeWindow(window.id)"
      @bring-to-front="bringToFront(window.id)"
    >
      <component :is="window.component" v-bind="window.props" />
    </MacWindow>
  </div>
</template>

<script setup lang="ts">
import { ref, h } from "vue";
import MacWindow from "../components/layouts/MacWindow.vue";
import BlogContent from "../components/content/BlogContent.vue";
import DiaryContent from "../components/content/DiaryContent.vue";
import GuestbookContent from "../components/content/GuestbookContent.vue";
import LoginForm from "../components/auth/LoginForm.vue";
import SignupForm from "../components/auth/SignupForm.vue";
import ExternalSite from "../components/content/ExternalSite.vue";
import { onMounted } from "vue";
import {
  Document,
  ChatDotRound,
  User,
  Notebook,
  Headset,
  Collection,
} from "@element-plus/icons-vue";
import ChatContent from "../components/content/ChatContent.vue";

onMounted(() => {
  window.addEventListener("open-menu", (e: any) => {
    openWindow(e.detail);
  });
});

interface Window {
  id: string;
  title: string;
  component: any;
  props: any;
  position: { x: number; y: number };
}

const activeWindows = ref<Window[]>([]);

const getRandomPosition = () => ({
  x: Math.random() * (window.innerWidth - 600),
  y: Math.random() * (window.innerHeight - 400),
});

const bringToFront = (windowId: string) => {
  const target = activeWindows.value.find((w) => w.id === windowId);
  if (!target) return;

  // 창 순서 재정렬 (맨 뒤로 보내기 = 가장 위로 올라오게)
  activeWindows.value = activeWindows.value.filter((w) => w.id !== windowId);
  activeWindows.value.push(target);
};

const openWindow = (menu: any) => {
  // Check if window is already open
  const existingWindow = activeWindows.value.find((w) => w.id === menu.name);
  if (existingWindow) {
    // Bring to front by removing and adding back
    activeWindows.value = activeWindows.value.filter((w) => w.id !== menu.name);
    activeWindows.value.push(existingWindow);
    return;
  }

  // Create new window
  const newWindow: Window = {
    id: menu.name,
    title: menu.title,
    component: getComponentForMenu(menu),
    props: {},
    position: getRandomPosition(),
  };

  activeWindows.value.push(newWindow);
};

const closeWindow = (windowId: string) => {
  activeWindows.value = activeWindows.value.filter((w) => w.id !== windowId);
};

const handleMouseOver = (e: MouseEvent) => {
  const target = e.currentTarget as HTMLElement;
  const dock = target.parentElement;
  if (!dock) return;

  const items = Array.from(dock.children) as HTMLElement[];
  const itemWidth = target.offsetWidth;
  const itemIndex = items.indexOf(target);
  const centerX = target.offsetLeft + itemWidth / 2;

  items.forEach((item, index) => {
    const distance = Math.abs(itemIndex - index);
    const scale = Math.max(1.3 - distance * 0.15, 1);
    const translateY = Math.max(-6 + Math.abs(distance) * 2, 0);
    item.style.transform = `scale(${scale}) translateY(${translateY}px)`;
  });
};

const handleMouseOut = (e: MouseEvent) => {
  const target = e.currentTarget as HTMLElement;
  const dock = target.parentElement;
  if (!dock) return;

  const items = Array.from(dock.children) as HTMLElement[];
  items.forEach((item) => {
    item.style.transform = "scale(1)";
  });
};

const getComponentForMenu = (menu: any) => {
  switch (menu.name) {
    case "posts":
      return {
        render() {
          return h(ExternalSite, { url: "https://blog.jsangmin.co.kr" });
        },
      };
    case "diary":
      return DiaryContent;
    case "guestbook":
      return ChatContent;
    case "login":
      return LoginForm;
    case "signup":
      return SignupForm;
    default:
      return "div";
  }
};

const menus = [
  {
    name: "posts",
    title: "블로그",
    icon: "/icons/Obsidian Icon.png",
  },
  {
    name: "guestbook",
    title: "채팅/방명록",
    icon: "/icons/book.png",
  },
  {
    name: "about",
    title: "소개",
    icon: "/icons/book.png",
  },
  {
    name: "diary",
    title: "다이어리",
    icon: "/icons/Calendar.png",
  },
  {
    name: "music",
    title: "뮤직플레이어",
    icon: "/icons/music.png",
  },
  {
    name: "book",
    title: "독서노트",
    icon: "/icons/book.png",
  },
  {
    name: "login",
    title: "로그인",
    icon: "/icons/Setting.png",
  },
];
</script>

<style scoped>
.home {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background-image: url("/src/assets/images/mac_background_1.jpg");
  background-size: cover;
  background-position: center;
}

.dock-container {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
}

.dock {
  display: flex;
  gap: 4px;
  padding: 4px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(25px) saturate(180%);
  -webkit-backdrop-filter: blur(25px) saturate(180%);
  border-radius: 18px;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.15), 0 1px 3px rgba(0, 0, 0, 0.1),
    0 3px 15px rgba(0, 0, 0, 0.1);
}

.dock-item {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 57px;
  height: 50px;
  transition: all 0.2s cubic-bezier(0.17, 0.17, 0, 1);
  cursor: pointer;
  margin: 0 2px;
}

.dock-icon {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.24) 0%, rgba(255, 255, 255, 0.1) 100%);
  border-radius: 14px;
  transition: all 0.2s cubic-bezier(0.17, 0.17, 0, 1);
  color: white;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.05), 0 2px 5px rgba(0, 0, 0, 0.1),
    0 5px 15px rgba(0, 0, 0, 0.1);
}

.dock-item:hover .dock-icon {
  transform: translateY(-10px) scale(1.1);
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0.15) 100%);
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.1), 0 5px 10px rgba(0, 0, 0, 0.15),
    0 10px 30px rgba(0, 0, 0, 0.1);
}

.dock-tooltip {
  position: absolute;
  top: -45px;
  left: 50%;
  transform: translateX(-50%) scale(0.95);
  background: rgba(0, 0, 0, 0.75);
  color: white;
  padding: 5px 12px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 500;
  opacity: 0;
  transition: all 0.15s ease;
  pointer-events: none;
  white-space: nowrap;
  backdrop-filter: blur(5px);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.dock-item:hover .dock-tooltip {
  opacity: 1;
  transform: translateX(-50%) scale(1);
  top: -40px;
}

/* Dock hover effect */
.dock:hover .dock-item {
  transform: translateY(0);
}

.dock:hover .dock-item:hover {
  transform: translateY(-10px);
}

/* Dock reflection effect */
.dock::after {
  content: "";
  position: absolute;
  bottom: -12px;
  left: 3%;
  right: 3%;
  height: 15px;
  background: linear-gradient(to bottom, rgba(255, 255, 255, 0.4), transparent);
  filter: blur(3px);
  border-radius: 50%;
  transform: scaleY(0.2);
  opacity: 0.5;
}
</style>
