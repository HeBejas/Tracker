import { ref } from 'vue'

interface Toast {
    id: number
    message: string
    type: 'success' | 'error' | 'warning'
    duration: number
}

const toasts = ref<Toast[]>([])
let idCounter = 0

export function useToast() {
    const addToast = (message: string, type: 'success' | 'error' | 'warning' = 'success', baseDuration: number = 3000) => {
        idCounter++

        const currentCount = toasts.value.length
        const finalDuration = baseDuration * Math.pow(1.5, currentCount)

        const newToast: Toast = { id: idCounter, message, type, duration: finalDuration }
        toasts.value.push(newToast)
    }

    const removeToast = (id: number) => {
        const index = toasts.value.findIndex(t => t.id === id)
        if (index !== -1) {
            toasts.value.splice(index, 1)
        }
    }

    const success = (message: string, duration?: number) => addToast(message, 'success', duration)
    const error = (message: string, duration?: number) => addToast(message, 'error', duration)
    const warning = (message: string, duration?: number) => addToast(message, 'warning', duration)

    return { toasts, removeToast, addToast, success, error, warning }
}