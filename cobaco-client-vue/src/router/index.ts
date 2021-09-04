import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import Home from "@/views/Home.vue";
import SignIn from "@/views/SignIn.vue";
import { profileStore, signOutAsync } from "@/store/profile";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: SignIn,
  },
  {
    path: "/home",
    name: "home",
    component: Home,
  },
  {
    path: "/sign-in",
    name: "sign-in",
    component: SignIn,
  },
];


const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.path === "/sign-in") {
    // サインイン中にアクセスした場合はサインアウトする。
    signOutAsync();
    next();
    return;
  }

  if (profileStore.profile) {
    next();
    return;
  }

  next("/sign-in");
});

export default router;
