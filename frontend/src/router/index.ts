import { createRouter, createWebHistory, RouterView } from 'vue-router'
import DashboardPage from '../pages/DashboardPage.vue'
import { useAuthStore } from '../stores/auth'

const routes = [
  { path: '/', redirect: '/login', meta: { HideLayout: true }},
  { path: '/login', name: 'Login', component: () => import('../pages/LoginPage.vue'), meta: { HideLayout: true, requiresAuth: false }},
  { path: '/home', name: 'Home', component: () => import('../pages/HomePage.vue'), meta: { breadcrumb: 'Главная', requiresAuth: true  }},
  { path: '/profile', name: 'Profile', component: () => import('../pages/ProfilePage.vue'), meta: { breadcrumb: 'Мой профиль', requiresAuth: true }},
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../pages/admin/AdminPanelPage.vue'),
    meta: { breadcrumb: 'Администрирование', requiresAuth: true  },
    children: [
      {
        path: '',
        redirect: '/admin/workspaces'
      },
      {
        path: 'workspaces',
        component: RouterView,
        meta: { breadcrumb: 'Рабочие среды' },
        children: [
          {
            path: '',
            name: 'AdminProjects',
            component: () => import('../pages/admin/WorkspacesList.vue'),
          },
          {
            path: ':id',
            name: 'WorkspacePage',
            component: () => import('../pages/admin/workspace/WorkspacePage.vue'),
            meta: { breadcrumb: (route) => `Рабочая среда №${route.params.id}` },
            children: [
              {
                path: '',
                redirect: to => `/admin/workspaces/${to.params.id}/dashboard`,

              },
              {
                path: 'dashboard',
                name: 'WorkspaceDashboard',
                component: () => import('../pages/admin/workspace/WorkspaceDashboard.vue'),
                meta: { breadcrumb: 'Дашборд' }
              },
              {
                path: 'tasks',
                name: 'WorkspaceTasks',
                component: () => import('../pages/admin/workspace/WorkspaceTasks.vue'),
                meta: { breadcrumb: 'Задачи' }
              },
              {
                path: 'settings',
                name: 'WorkspaceSettings',
                component: () => import('../pages/admin/workspace/WorkspaceSettings.vue'),
                meta: { breadcrumb: 'Настройки' }
              }
            ]
          }
        ]
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('../pages/admin/UsersList.vue'),
        meta: { breadcrumb: 'Пользователи' }
      },
      {
        path: 'tariffs',
        name: 'AdminTariffs',
        component: () => import('../pages/admin/TariffsList.vue'),
        meta: { breadcrumb: 'Тарифы' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return '/login'
  } else if (to.path === '/login' && authStore.isAuthenticated) {
    return '/home'
  } else if (to.meta.requiresAdmin && !authStore.isAdmin) {
    return '/home'
  }
})

export default router