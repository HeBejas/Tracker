import { createRouter, createWebHistory, RouterView } from 'vue-router'
import DashboardPage from '../pages/DashboardPage.vue'
import { useAuthStore } from '../stores/auth'
import type { RouteLocationNormalized, RouteLocation } from 'vue-router'

import { ref } from 'vue'
export const currentProjectName = ref('')
export const currentTaskName = ref('')

const routes = [
  { path: '/', redirect: '/login', meta: { HideLayout: true }},
  { path: '/login', name: 'Login', component: () => import('../pages/LoginPage.vue'), meta: { HideLayout: true, requiresAuth: false }},
  { path: '/profile', name: 'Profile', component: () => import('../pages/ProfilePage.vue'), meta: { breadcrumb: 'Мой профиль', requiresAuth: true }},
  // { path: '/home', name: 'Home', component: () => import('../pages/HomePage.vue'), meta: { breadcrumb: 'Главная', requiresAuth: true  }},
  {
    path: '/workspaces/:id',
    component: () => import('../pages/workspace/WorkspaceLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'WorkspaceDashboardRedirect',
        redirect: (to: RouteLocation) => `/workspaces/${to.params.id}/dashboard`,
      },
      {
        path: 'dashboard',
        name: 'DashboardPage',
        component: () => import('../pages/workspace/DashboardPage.vue'),
        meta: { breadcrumb: 'Дашборд' }
      },

      // Вложенная структура для проектов
      {
        path: 'projects',
        component: RouterView,
        meta: { breadcrumb: 'Проекты'},
        children: [
          {
            path: '',
            name: 'ProjectsList',
            component: () => import('../pages/workspace/ProjectsList.vue'),
          },
          {
            path: ':projectId',
            component: RouterView,
            meta: {
              breadcrumb: (route: RouteLocationNormalized) => {
                return currentProjectName.value || `Проект ${route.params.projectId}`
              }
            },
            children: [
              {
                path: '',
                component: () => import('../pages/workspace/project/ProjectLayout.vue'),
                meta: { showChildTabs: true },
                children: [
                  {
                    path: '',
                    name: 'ProjectPage',
                    component: () => import('../pages/workspace/project/ProjectPage.vue'),
                    meta: { breadcrumb: 'О проекте' }
                  },
                  {
                    path: 'tasks',
                    name: 'ProjectTasks',
                    meta: { breadcrumb: 'Задачи' },
                    component: () => import('../pages/workspace/project/ProjectTasksList.vue')
                  }
                ]
              },
              {
                path: 'tasks',
                component: RouterView,
                meta: { breadcrumb: 'Задачи' },
                children: [
                  {
                    path: ':taskId',
                    component: () => import('../pages/workspace/task/TaskLayout.vue'),
                    meta: {
                      breadcrumb: (route: RouteLocationNormalized) => {
                        return currentTaskName.value || `Задача #${route.params.taskId}`
                      },
                      showChildTabs: true
                    },
                    children: [
                      {
                        path: '',
                        name: 'ProjectTaskPage',
                        component: () => import('../pages/workspace/task/TaskPage.vue'),
                        meta: { breadcrumb: 'О задаче' }
                      }
                    ]
                  }
                ]
              }

            ]
          }
        ]
      },
      {
        path: 'reports',
        name: 'ReportsList',
        component: () => import('../pages/workspace/ReportsList.vue'),
        meta: { breadcrumb: 'Отчеты' }
      },
      {
        path: 'settings',
        name: 'WorkspaceSettings',
        component: () => import('../pages/workspace/WorkspaceSettings.vue'),
        meta: { breadcrumb: 'Настройки' }
      }
    ]
  },
  {
    path: '/admin',
    component: () => import('../pages/admin/AdminPanelPage.vue'),
    meta: { breadcrumb: 'Администрирование', requiresAuth: true  },
    children: [
      {
        path: '',
        name: 'Admin',
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
            component: () => import('../pages/admin/WorkspacePage.vue'),
            meta: { breadcrumb: (route: RouteLocationNormalized) => `Рабочая среда №${route.params.id}` },
            children: [
              {
                path: '',
                redirect: (to: RouteLocation) => `/admin/workspaces/${to.params.id}/dashboard`,

              },
              {
                path: 'dashboard',
                name: 'WorkspaceDashboard',
                component: () => import('../pages/workspace/DashboardPage.vue'),
                meta: { breadcrumb: 'Дашборд' }
              },
              {
                path: 'projects',
                name: 'AdminProjectsList',
                component: () => import('../pages/workspace/ProjectsList.vue'),
                meta: { breadcrumb: 'Проекты' }
              },
              {
                path: 'employees',
                name: 'AdminEmployeesList',
                component: () => import('../pages/workspace/EmployeesList.vue'),
                meta: { breadcrumb: 'Сотрудники' }
              },
              {
                path: 'reports',
                name: 'WorkspaceReports',
                component: () => import('../pages/workspace/ReportsList.vue'),
                meta: { breadcrumb: 'Отчеты' }
              },
              {
                path: 'settings',
                name: 'WorkspaceSettings',
                component: () => import('../pages/workspace/WorkspaceSettings.vue'),
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
    if (authStore.userRoleId === 1) {
      return '/admin/workspaces'
    }
    return `/workspaces/${authStore.userWorkspaceId}/dashboard`
  } else if (to.meta.requiresAdmin && authStore.userRoleId !== 1) {
    return `/workspaces/${authStore.userWorkspaceId}/dashboard`
  }
})

export default router