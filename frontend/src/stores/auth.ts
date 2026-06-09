import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || null,
        userId: localStorage.getItem('userId') ? Number(localStorage.getItem('userId')) : null,
        userRole: localStorage.getItem('userRole') ? String(localStorage.getItem('userRole')) : null,
        fullName: localStorage.getItem('fullName') || null,
        email: localStorage.getItem('email') || null,
        workspaceId: localStorage.getItem('workspaceId') || null,
        isAuthenticated: false
    }),
    getters: {
        isAuthenticated: (state) => !!state.token,
        isAdmin: (state) => state.userRole === 'admin'
    },
    actions: {
        login(token: any, userId: any, userRole: any, fullName: any, email: any, workspaceId: number | null) {
            this.token = token
            this.userId = userId
            this.userRole = String(userRole)
            this.fullName = fullName
            this.email = email
            this.workspaceId = workspaceId || null
            localStorage.setItem('token', token)
            localStorage.setItem('userRole', userRole)
            localStorage.setItem('userId', String(userId))
            localStorage.setItem('fullName', fullName)
            localStorage.setItem('email', email)
            localStorage.setItem('workspaceId', workspaceId)
            this.isAuthenticated = true
        },
        logout() {
            this.token = null
            this.userRole = null
            this.userId = null
            this.fullName = null
            this.email = null
            this.workspaceId = null
            this.isAuthenticated = false
            localStorage.removeItem('token')
            localStorage.removeItem('userRole')
            localStorage.removeItem('userId')
            localStorage.removeItem('fullName')
            localStorage.removeItem('email')
            localStorage.removeItem('workspaceId')
        }
    }
})