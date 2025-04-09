<template>
  <Transition name="window">
    <div
      v-if="!isMinimized"
      class="window"
      :class="{ maximized: isMaximized }"
      :style="windowStyle"
      @mousedown="handleMouseDown"
      @mousemove="handleMouseMove"
      @mouseleave="handleMouseLeave"
    >
      <div class="window-header" @mousedown="startDrag">
        <div class="window-controls">
          <div class="control close" @click="closeWindow"></div>
          <div class="control minimize" @click="minimizeWindow"></div>
          <div class="control maximize" @click="maximizeWindow"></div>
        </div>
        <div class="window-title">{{ title }}</div>
      </div>
      <div class="window-content">
        <slot></slot>
      </div>
    </div>
  </Transition>
</template>

<script setup lang="ts">
import { ref, computed, nextTick } from "vue";

const emit = defineEmits<{
  (e: "close"): void;
  (e: "bring-to-front"): void;
}>();

const props = defineProps<{
  title: string;
  initialPosition?: { x: number; y: number };
}>();

const position = ref(props.initialPosition || { x: 100, y: 100 });
const isDragging = ref(false);
const dragOffset = ref({ x: 0, y: 0 });
const isMaximized = ref(false);
const isMinimized = ref(false);
const previousPosition = ref({ x: 0, y: 0 });
const previousSize = ref({ width: 600, height: 400 });

// 리사이즈 관련 상태
const isResizing = ref(false);
const resizeEdge = ref("");
const startSize = ref({ width: 0, height: 0 });
const startPosition = ref({ x: 0, y: 0 });
const minSize = { width: 300, height: 200 };

const windowStyle = computed(() => {
  if (isMaximized.value) {
    return {
      left: "0px",
      top: "0px",
      width: "100vw",
      height: "100vh",
    };
  }
  return {
    left: position.value.x + "px",
    top: position.value.y + "px",
    width: previousSize.value.width + "px",
    height: previousSize.value.height + "px",
  };
});

const startDrag = async (e: MouseEvent) => {
  if (e.target === e.currentTarget) {
    dragOffset.value = {
      x: e.clientX - position.value.x,
      y: e.clientY - position.value.y,
    };

    if (isMaximized.value) {
      // 현재 화면 기준 비율 위치
      const ratioX = e.clientX / window.innerWidth;

      // 상태 복원
      isMaximized.value = false;
      isSnapped.value = false;

      position.value = {
        x: e.clientX - previousSize.value.width * ratioX,
        y: 30, // 윈도우 헤더만 보이도록 아래로 약간 이동
      };

      await nextTick(); // DOM 업데이트 기다림
    }

    isDragging.value = true;
    document.addEventListener("mousemove", onMouseMove);
    document.addEventListener("mouseup", stopDrag);
  }
};

const SNAP_MARGIN = 20;

const isSnapped = ref(false);

const onMouseMove = (e: MouseEvent) => {
  if (isDragging.value) {
    const newX = e.clientX - dragOffset.value.x;
    const newY = e.clientY - dragOffset.value.y;

    // 상단 최대화
    if (newY <= SNAP_MARGIN) {
      saveCurrentState();
      maximizeWindow();
      isSnapped.value = true;
      stopDrag();
      return;
    }

    // 좌측 반 화면
    if (newX <= SNAP_MARGIN) {
      saveCurrentState();
      isMaximized.value = false;
      isSnapped.value = true;
      position.value = { x: 0, y: 0 };
      previousSize.value = { width: window.innerWidth / 2, height: window.innerHeight };
      stopDrag();
      return;
    }

    // 우측 반 화면
    if (newX >= window.innerWidth - SNAP_MARGIN - previousSize.value.width) {
      saveCurrentState();
      isMaximized.value = false;
      isSnapped.value = true;
      position.value = { x: window.innerWidth / 2, y: 0 };
      previousSize.value = { width: window.innerWidth / 2, height: window.innerHeight };
      stopDrag();
      return;
    }

    // 일반 드래그 → 스냅 해제
    if (isSnapped.value) {
      restoreFromSnap(); // 아래 함수로 상태 복구
    }

    position.value = { x: newX, y: newY };
  }
};

const saveCurrentState = () => {
  previousPosition.value = { ...position.value };
  previousSize.value = { ...previousSize.value };
};

const restoreFromSnap = () => {
  isSnapped.value = false;
  isMaximized.value = false;
  previousSize.value = { width: 600, height: 400 }; // 또는 이전 저장된 값 사용
};

const onResize = (e: MouseEvent) => {
  if (!isResizing.value) return;

  const dx = e.clientX - startPosition.value.x;
  const dy = e.clientY - startPosition.value.y;
  let newWidth = startSize.value.width;
  let newHeight = startSize.value.height;
  let newX = position.value.x;
  let newY = position.value.y;

  if (resizeEdge.value.includes("right")) {
    newWidth = Math.max(startSize.value.width + dx, minSize.width);
  }
  if (resizeEdge.value.includes("bottom")) {
    newHeight = Math.max(startSize.value.height + dy, minSize.height);
  }
  if (resizeEdge.value.includes("left")) {
    const proposedWidth = startSize.value.width - dx;
    if (proposedWidth >= minSize.width) {
      newWidth = proposedWidth;
      newX = startPosition.value.x + dx;
    }
  }
  if (resizeEdge.value.includes("top")) {
    const proposedHeight = startSize.value.height - dy;
    if (proposedHeight >= minSize.height) {
      newHeight = proposedHeight;
      newY = startPosition.value.y + dy;
    }
  }

  previousSize.value = { width: newWidth, height: newHeight };
  position.value = { x: newX, y: newY };
};

const handleMouseDown = (e: MouseEvent) => {
  if (isMaximized.value) return;

  const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
  const edge = getResizeEdge(e, rect);

  if (edge) {
    e.preventDefault(); // 텍스트 선택 방지
    isResizing.value = true;
    resizeEdge.value = edge;
    startSize.value = { ...previousSize.value };
    startPosition.value = { x: e.clientX, y: e.clientY };
    document.addEventListener("mousemove", onResize);
    document.addEventListener("mouseup", stopDrag);
  }
  emit("bring-to-front");
};

const getResizeEdge = (e: MouseEvent, rect: DOMRect): string => {
  const edgeSize = 8;
  const x = e.clientX - rect.left;
  const y = e.clientY - rect.top;
  const width = rect.width;
  const height = rect.height;

  const isTop = y < edgeSize;
  const isBottom = y > height - edgeSize;
  const isLeft = x < edgeSize;
  const isRight = x > width - edgeSize;

  if (isTop && isLeft) return "top-left";
  if (isTop && isRight) return "top-right";
  if (isBottom && isLeft) return "bottom-left";
  if (isBottom && isRight) return "bottom-right";
  if (isTop) return "top";
  if (isBottom) return "bottom";
  if (isLeft) return "left";
  if (isRight) return "right";
  return "";
};

const handleMouseMove = (e: MouseEvent) => {
  if (isMaximized.value || isResizing.value || isDragging.value) return;

  const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
  const edge = getResizeEdge(e, rect);
  const target = e.currentTarget as HTMLElement;

  if (edge) {
    target.style.cursor = getCursorStyle(edge);
  } else if (e.target === target) {
    target.style.cursor = "default";
  }
};

const handleMouseLeave = (e: MouseEvent) => {
  if (!isResizing.value && !isDragging.value) {
    const target = e.currentTarget as HTMLElement;
    target.style.cursor = "default";
  }
};

const getCursorStyle = (edge: string): string => {
  switch (edge) {
    case "top":
    case "bottom":
      return "row-resize";
    case "left":
    case "right":
      return "col-resize";
    case "top-left":
    case "bottom-right":
      return "nwse-resize";
    case "top-right":
    case "bottom-left":
      return "nesw-resize";
    default:
      return "default";
  }
};

const stopDrag = () => {
  isDragging.value = false;
  isResizing.value = false;
  document.removeEventListener("mousemove", onMouseMove);
  document.removeEventListener("mousemove", onResize);
  document.removeEventListener("mouseup", stopDrag);
  document.body.style.cursor = "default";
};

const closeWindow = () => {
  emit("close");
};

const minimizeWindow = () => {
  isMinimized.value = true;
  setTimeout(() => {
    isMinimized.value = false;
  }, 300);
};

const maximizeWindow = () => {
  if (!isMaximized.value) {
    previousPosition.value = { ...position.value };
    isMaximized.value = true;
  } else {
    position.value = { ...previousPosition.value };
    isMaximized.value = false;
  }
};
</script>

<style scoped>
.window {
  position: fixed;
  width: 600px;
  height: 400px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  user-select: none;
  z-index: 10;
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.window.maximized {
  border-radius: 0;
  width: 100vw;
  height: 100vh;
}

.window-enter-active,
.window-leave-active {
  transition: all 0.3s ease;
}

.window-enter-from,
.window-leave-to {
  transform: scale(0.8);
  opacity: 0;
}

.window-enter-to,
.window-leave-from {
  transform: scale(1);
  opacity: 1;
}

.window-header {
  height: 30px;
  background: #333;
  display: flex;
  align-items: center;
  padding: 0 10px;
  cursor: move;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

.window-controls {
  display: flex;
  gap: 8px;
}

.control {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  cursor: pointer;
}

.close {
  background: #ff5f56;
}

.minimize {
  background: #ffbd2e;
}

.maximize {
  background: #27c93f;
}

.window-title {
  color: white;
  margin-left: 10px;
  font-size: 12px;
}

.window-content {
  position: relative;
  width: 100%;
  height: calc(100% - 30px);
  overflow: auto;
}
</style>
