import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import About from '@/views/Admin.vue'
import Admin from "@/views/Admin.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    props: route => ({pigeonId: route.query.id})
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
