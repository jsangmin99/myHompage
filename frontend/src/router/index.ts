import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";
import PostsView from "../views/PostsView.vue";
import AboutView from "../views/AboutView.vue";
import GuestBookView from "../views/GuestBookView.vue";
import DiaryView from "../views/DiaryView.vue";
import MusicView from "../views/MusicView.vue";
import BookView from "../views/BookView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/posts",
    name: "posts",
    component: PostsView,
  },
  {
    path: "/about",
    name: "about",
    component: AboutView,
  },
  {
    path: "/guestbook",
    name: "guestbook",
    component: GuestBookView,
  },
  {
    path: "/diary",
    name: "diary",
    component: DiaryView,
  },
  {
    path: "/music",
    name: "music",
    component: MusicView,
  },
  {
    path: "/book",
    name: "book",
    component: BookView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
