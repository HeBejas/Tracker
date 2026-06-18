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
                  },
                  {
                    path: 'settings',
                    name: 'ProjectSettings',
                    component: () => import('../pages/workspace/project/ProjectSettings.vue'),
                    meta: { breadcrumb: 'Настройки' }
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
                      },
                      {
                        path: 'settings',
                        name: 'TaskSettings',
                        component: () => import('../pages/workspace/task/TaskSettings.vue'),
                        meta: { breadcrumb: 'Настройки' }
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
        path: 'my-tasks',
        name: 'MyTasksList',
        component: () => import('../pages/workspace/MyTasksList.vue'),
        meta: { breadcrumb: 'Мои задачи' }
      },
      {
        path: 'settings',
        name: 'WorkspaceSettings',
        component: () => import('../pages/workspace/WorkspaceSettings.vue'),
        meta: { breadcrumb: 'Настройки' }
      },
      {
        path: 'tasks',
        component: RouterView,
        meta: { breadcrumb: 'Задачи' },
        children: [
          {
            path: '',
            name: 'TasksPage', // Страница со списком всех задач
            component: () => import('../pages/workspace/TasksList.vue'),
          },
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
                name: 'WorkspaceTaskPage',
                component: () => import('../pages/workspace/task/TaskPage.vue'),
                meta: { breadcrumb: 'О задаче' }
              },
              {
                path: 'settings',
                name: 'WorkspaceTaskSettings',
                component: () => import('../pages/workspace/task/TaskSettings.vue'),
                meta: { breadcrumb: 'Настройки' }
              }
            ]
          }
        ]
      },
      {
        path: 'employees',
        name: 'EmployeesList',
        component: () => import('../pages/workspace/EmployeesList.vue'),
        meta: { breadcrumb: 'Сотрудники' }
      },
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
                redirect: (to: RouteLocation) => `/admin/workspaces/${to.params.id}/projects`,

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
    if (Number(authStore.userRole) === 1) {
      return '/admin/workspaces'
    }
    return `/workspaces/${authStore.workspaceId}/dashboard`
  } else if (to.meta.requiresAdmin && Number(authStore.userRole) !== 1) {
    return `/workspaces/${authStore.workspaceId}/dashboard`
  }
})

export default router