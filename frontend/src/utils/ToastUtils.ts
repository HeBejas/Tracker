import { ref } from 'vue'

const toasts = ref([])
let idCounter = 0

export function useToast() {
    const addToast = (message, type = 'success', baseDuration = 3000) => {
        idCounter++

        const currentCount = toasts.value.length
        const finalDuration = baseDuration * Math.pow(1.5, currentCount)

        const newToast = { id: idCounter, message, type, duration: finalDuration }
        toasts.value.push(newToast)
    }

    const removeToast = (id) => {
        const index = toasts.value.findIndex(t => t.id === id)
        if (index !== -1) {
            toasts.value.splice(index, 1)
        }
    }

    const success = (message, duration = 3000) => addToast(message, 'success', duration)
    const error = (message, duration = 3000) => addToast(message, 'error', duration)
    const warning = (message, duration = 3000) => addToast(message, 'warning', duration)

    return { toasts, removeToast, addToast, success, error, warning }
}