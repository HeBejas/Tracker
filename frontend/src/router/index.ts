import { createRouter, createWebHistory } from 'vue-router'
import DashboardPage from '../pages/DashboardPage.vue'

const routes = [
  { path: '/', redirect: '/login', meta: { requiresLayout: false }},
  { path: '/login', name: 'Login', component: () => import('../pages/LoginPage.vue')  },
  { path: '/home', name: 'Home', component: () => import('../pages/HomePage.vue'), meta: { breadcrumb: 'Главная' } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router