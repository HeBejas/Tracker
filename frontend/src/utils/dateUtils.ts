export const formatUIDate = (dateString: string | undefined): string => {
    if (!dateString) return ''

    const dateObj = new Date(dateString)

    const day = String(dateObj.getDate()).padStart(2, '0')
    const month = String(dateObj.getMonth() + 1).padStart(2, '0')
    const year = dateObj.getFullYear()

    const hours = String(dateObj.getHours()).padStart(2, '0')
    const minutes = String(dateObj.getMinutes()).padStart(2, '0')

    return `${day}.${month}.${year}, ${hours}:${minutes}`
}

export const formatDateForFilter = (dateString: string): string => {
    if (!dateString) return ''

    const dateObj = new Date(dateString)
    const y = dateObj.getFullYear()
    const m = String(dateObj.getMonth() + 1).padStart(2, '0')
    const d = String(dateObj.getDate()).padStart(2, '0')

    return `${y}-${m}-${d}`
}