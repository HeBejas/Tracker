import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || null,
        userRole: localStorage.getItem('userRole') ? String(localStorage.getItem('userRole')) : null
    }),
    getters: {
        isAuthenticated: (state) => !!state.token,
        isAdmin: (state) => state.userRole === 'ADMIN'
    },
    actions: {
        login(token, userRole) {
            this.token = token
            this.userRole = String(userRole)
            localStorage.setItem('token', token)
            localStorage.setItem('userRole', userRole)
        },
        logout() {
            this.token = null
            this.userRole = null
            localStorage.removeItem('token')
            localStorage.removeItem('userRole')
        }
    }
})